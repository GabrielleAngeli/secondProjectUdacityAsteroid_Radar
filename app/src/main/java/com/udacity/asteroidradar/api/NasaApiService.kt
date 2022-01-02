package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Enum full of constants to match the query values our web service expects
 */
enum class AsteroidFilter(val value: String){SHOW_WEEK("week"), SHOW_TODAY("today"), SHOW_SAVE("saved")}


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()

interface NasaApiService {

    /**
     * @GET request for asteroid list
     */
    @GET("neo/rest/v1/feed")
    fun getAsteroids(@Query("start_date")
                                 startDate: String,
                          @Query("end_date")
                                 endDate: String,
                          @Query("api_key")
                                 api_key: String = Constants.API_KEY): String

    /**
     * @GET request for picture of the day
     */
    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") apiKey: String): PictureOfDay
}

object NasaApi {
    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}