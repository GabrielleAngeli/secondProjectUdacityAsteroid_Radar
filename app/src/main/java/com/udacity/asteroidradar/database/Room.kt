package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Holds all the necessary methods to access the database.
 */
@Dao
interface AsteroidDao {

    @Query("select * from DatabaseAsteroid order by closeApproachDate desc")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from DatabaseAsteroid where closeApproachDate = :today order by closeApproachDate desc")
    fun getTodayAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from DatabaseAsteroid where closeApproachDate between :startDay and :endDay order by closeApproachDate desc")
    fun getWeekAsteroids(startDay: String, endDay: String): LiveData<List<DatabaseAsteroid>>

    // If the same asteroids are inserted then they will be replaced
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)
}

/**
 * Room database
 */
@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

// Singleton, meaning only one instance can be used and created
private lateinit var INSTANCE: AsteroidDatabase

/**
 * Function that gets and returns the AsteroidDatabase
 */
fun getDatabase(context: Context): AsteroidDatabase {

    // Initialization is thread-safe when wrapped in synchronized
    synchronized(AsteroidDatabase::class.java) {
        // Checks whether the database has been initialized
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}
