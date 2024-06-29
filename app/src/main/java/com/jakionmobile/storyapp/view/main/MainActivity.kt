package com.jakionmobile.storyapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakionmobile.storyapp.R
import com.jakionmobile.storyapp.data.ResultState
import com.jakionmobile.storyapp.databinding.ActivityMainBinding
import com.jakionmobile.storyapp.view.SectionsPageAdapter
import com.jakionmobile.storyapp.view.ViewModelFactory
import com.jakionmobile.storyapp.view.map.MapsActivity
import com.jakionmobile.storyapp.view.uploadstory.UploadStoryActivity
import com.jakionmobile.storyapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var sectionsPageAdapter: SectionsPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                setupView()
                setupAction()
                setupRecyclerView()
                setupFab()
            }
        }


//        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupRecyclerView() {
        sectionsPageAdapter = SectionsPageAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = sectionsPageAdapter
    }

    private fun setupAction() {
        viewModel.getStories().observe(this) {
            sectionsPageAdapter.submitData(lifecycle, it)
        }
    }

//    private fun setupAction() {
//        viewModel.getStories().observe(this) { story ->
//            when (story) {
//                is ResultState.Error -> {
//                    binding.progressBar.visibility = View.INVISIBLE
//                    val error = story.error
//                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
//                }
//
//                is ResultState.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//
//                is ResultState.Success -> {
//                    binding.progressBar.visibility = View.INVISIBLE
//                    story.data?.let {
//                        sectionsPageAdapter.submitData(lifecycle, it)
//                    }
//                }
//            }
//        }
//    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                lifecycleScope.launch {
                    viewModel.logout()
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                }
            }

            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.map -> {
                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
//    private fun playAnimation() {
//        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
//            duration = 6000
//            repeatCount = ObjectAnimator.INFINITE
//            repeatMode = ObjectAnimator.REVERSE
//        }.start()
//
//        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
//        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
//        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(100)
//
//        AnimatorSet().apply {
//            playSequentially(name, message, logout)
//            startDelay = 100
//        }.start()
//    }
}