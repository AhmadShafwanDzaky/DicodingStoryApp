package com.jakionmobile.storyapp.di

import android.content.Context
import com.jakionmobile.storyapp.data.UserRepository
import com.jakionmobile.storyapp.data.api.ApiConfig
import com.jakionmobile.storyapp.data.api.AuthApiService
import com.jakionmobile.storyapp.data.api.StoryApiService
import com.jakionmobile.storyapp.data.pref.UserPreference
import com.jakionmobile.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val authApiService: AuthApiService = ApiConfig.getAuthApiService()
        val storyApiService: StoryApiService = ApiConfig.getStoryApiService(user.token)
        return UserRepository.getInstance(pref, authApiService, storyApiService)
    }
}