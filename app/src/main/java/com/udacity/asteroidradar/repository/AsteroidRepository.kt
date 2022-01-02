package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Repository for refreshing and loading asteroids
 */
class AsteroidRepository(private val database: AsteroidDatabase) {

    private var asteroidFilter = MutableLiveData(AsteroidFilter.SHOW_TODAY)

    // LiveData for asteroids displayed in RecyclerView
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.switchMap(asteroidFilter) { menuFilter ->
            when (menuFilter) {
                AsteroidFilter.SHOW_TODAY -> Transformations.map(
                    database.asteroidDao.getTodayAsteroids(
                        getStartDateFormatted()
                    )
                ) {
                    it.asDomainModel()
                }
                AsteroidFilter.SHOW_WEEK -> Transformations.map(
                    database.asteroidDao.getWeekAsteroids(
                        getStartDateFormatted(),
                        getEndDateFormatted()
                    )
                ) {
                    it.asDomainModel()
                }
                else -> Transformations.map(database.asteroidDao.getAllAsteroids()) {
                    it.asDomainModel()
                }
            }
        }

    suspend fun refreshAsteroids() {
        try {
            withContext(Dispatchers.IO) {
                val asteroidsRefresh =
                    NasaApi.retrofitService.getAsteroids(
                        getStartDateFormatted(),
                        getEndDateFormatted(),
                        Constants.API_KEY
                    )
                val jsonParsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsRefresh))

                database.asteroidDao.insertAll(*jsonParsedAsteroids.asDatabaseModel())
            }
        } catch (e: Exception) {
            Log.e("repo", "refreshAsteroids")
        }
    }

    fun getAsteroidsFiltered(filter: AsteroidFilter) {
        asteroidFilter.value = filter
    }
}



