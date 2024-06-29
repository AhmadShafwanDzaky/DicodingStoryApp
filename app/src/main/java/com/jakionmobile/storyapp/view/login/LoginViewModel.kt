package com.jakionmobile.storyapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakionmobile.storyapp.data.UserRepository
import com.jakionmobile.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)

    suspend fun saveSession(userModel: UserModel) {
        repository.saveSession(userModel)
    }
}