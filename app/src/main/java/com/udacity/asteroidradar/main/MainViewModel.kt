package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidFilter
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData for the daily image
    private val _dailyPicture =
        MutableLiveData<PictureOfDay>()
    val dailyPicture: LiveData<PictureOfDay>
        get() = _dailyPicture

    //LiveData for navigation
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    // Database reference
    private val database = getDatabase(application)

    // Asteroid LiveData
    private val asteroidRepository = AsteroidRepository(database)
    val asteroidsForFragment = asteroidRepository.asteroids

    var loading = false
    init {
        getPictureOfDayResponse()
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    private fun getPictureOfDayResponse() {
        viewModelScope.launch {
            try {
                // Grabs picture from retrofit and assigns it to mutable livedata variable
                var tempPictureOfDay =
                    NasaApi.retrofitService.getPictureOfDay(Constants.API_KEY)
                _dailyPicture.value = tempPictureOfDay
            } catch (e: Exception) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }

    /**
     * Takes an asteroid and assigns it to the value
     */
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }


    fun displayAsteroidCompleted() {
        _navigateToSelectedAsteroid.value = null
    }

    /**
     * Updates filter according to enum parameters
     */
    fun updateFilter(filter: AsteroidFilter) {
        asteroidRepository.getAsteroidsFiltered(filter)
        Log.i("MainViewModel", "updateFilter called")
    }
}