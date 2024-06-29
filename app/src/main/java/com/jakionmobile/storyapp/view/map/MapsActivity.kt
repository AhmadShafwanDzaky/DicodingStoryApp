package com.jakionmobile.storyapp.view.map

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.jakionmobile.storyapp.R
import com.jakionmobile.storyapp.data.ResultState
import com.jakionmobile.storyapp.databinding.ActivityMapsBinding
import com.jakionmobile.storyapp.view.ViewModelFactory
import com.jakionmobile.storyapp.view.main.MainViewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        getStoriesAndAddMarkers()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun getStoriesAndAddMarkers() {
        viewModel.getStoriesWithLocation().observe(this, Observer { result ->
            when (result) {
                is ResultState.Loading -> {
                }
                is ResultState.Success -> {
                    val cameraPosition = LatLng(6.1750,106.8283)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 2f))
                    result.data.listStory.forEach { data ->
                        val latLng = LatLng(data.lat ?: 6.1750, data.lon ?: 106.8283)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(data.name)
                                .snippet(data.description)
                        )
                    }
                }
                is ResultState.Error -> {
                    Log.e(TAG, "Error fetching stories: ${result.error}")
                }
            }
        })
    }

    companion object {
        private const val TAG = "MapsActivity"
    }

}