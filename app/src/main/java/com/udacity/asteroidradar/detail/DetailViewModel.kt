package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R

class DetailViewModel(asteroid: Asteroid, app: Application) : AndroidViewModel(app) {
    val resources = getApplication<Application>().resources

    private val _selectedItem = MutableLiveData<Asteroid>()

    init {
        _selectedItem.value = asteroid
    }

    // Assigns string according to hazard status
    val contentDescription = when (_selectedItem.value!!.isPotentiallyHazardous) {
        true -> resources.getString(R.string.potentially_hazardous_asteroid_image)
        else -> resources.getString(R.string.not_hazardous_asteroid_image)
    }
}