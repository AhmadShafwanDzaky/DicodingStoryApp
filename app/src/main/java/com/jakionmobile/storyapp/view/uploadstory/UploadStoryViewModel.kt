package com.jakionmobile.storyapp.view.uploadstory

import androidx.lifecycle.ViewModel
import com.jakionmobile.storyapp.data.UserRepository
import java.io.File

class UploadStoryViewModel(private val repository: UserRepository) : ViewModel() {
    fun uploadStory(file: File, description: String) = repository.uploadStory(file, description)
}