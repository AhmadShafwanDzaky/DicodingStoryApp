package com.jakionmobile.storyapp.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jakionmobile.storyapp.data.ResultState
import com.jakionmobile.storyapp.data.UserRepository
import com.jakionmobile.storyapp.data.api.StoryResponse

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    fun getStoriesWithLocation(): LiveData<ResultState<StoryResponse>> {
        return repository.getStoriesWithLocation()
    }
}