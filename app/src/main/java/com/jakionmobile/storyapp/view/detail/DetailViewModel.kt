package com.jakionmobile.storyapp.view.detail

import androidx.lifecycle.ViewModel
import com.jakionmobile.storyapp.data.UserRepository

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    fun getStoryDetail(id: String) = repository.getStoryDetail(id)
}