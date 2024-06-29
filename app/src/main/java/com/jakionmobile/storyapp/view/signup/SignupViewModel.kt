package com.jakionmobile.storyapp.view.signup

import androidx.lifecycle.ViewModel
import com.jakionmobile.storyapp.data.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.signUp(name, email, password)
}