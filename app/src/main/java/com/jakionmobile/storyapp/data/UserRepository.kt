package com.jakionmobile.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.jakionmobile.storyapp.data.api.ApiConfig
import com.jakionmobile.storyapp.data.api.AuthApiService
import com.jakionmobile.storyapp.data.api.ErrorResponse
import com.jakionmobile.storyapp.data.api.ListStoryItem
import com.jakionmobile.storyapp.data.api.LoginResponse
import com.jakionmobile.storyapp.data.api.RegisterResponse
import com.jakionmobile.storyapp.data.api.Story
import com.jakionmobile.storyapp.data.api.StoryApiService
import com.jakionmobile.storyapp.data.pref.UserModel
import com.jakionmobile.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val authApiService: AuthApiService,
    private var storyApiService: StoryApiService,
) {

    fun signUp(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = authApiService.register(name, email, password)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(ResultState.Error(errorMessage))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = authApiService.login(email, password)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(ResultState.Error(errorMessage))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(storyApiService)
            }
        ).liveData
    }

    fun getStoryDetail(id: String): LiveData<ResultState<Story?>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = storyApiService.getStoryDetail(id)
            emit(ResultState.Success(response.story))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun uploadStory(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = storyApiService.uploadImage(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun getStoriesWithLocation() = liveData {
        emit(ResultState.Loading)
        try {
            val response = storyApiService.getStoriesWithLocation()
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unknown error occurred"))
        }
    }

    suspend fun saveSession(user: UserModel) {
        storyApiService = ApiConfig.getStoryApiService(user.token)
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            authApiService: AuthApiService,
            storyApiService: StoryApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, authApiService, storyApiService)
            }.also { instance = it }
    }
}