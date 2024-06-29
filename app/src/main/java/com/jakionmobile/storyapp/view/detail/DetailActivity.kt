package com.jakionmobile.storyapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.jakionmobile.storyapp.R
import com.jakionmobile.storyapp.data.ResultState
import com.jakionmobile.storyapp.databinding.ActivityDetailBinding
import com.jakionmobile.storyapp.view.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_story)

        val storyId = intent.getStringExtra(EXTRA_STORY_ID)
        if (storyId != null) {
            viewModel.getStoryDetail(storyId).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val story = result.data
                        // Update UI with story details
                        binding.tvStoryTitle.text = story?.name ?: "name"
                        binding.tvStoryDescription.text = story?.description ?: "desc"
                        Glide.with(applicationContext)
                            .load(story?.photoUrl)
                            .into(binding.ivItemPhotoDetail)
                        // Load image if available
                    }

                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}