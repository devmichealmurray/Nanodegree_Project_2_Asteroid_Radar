package com.udacity.asteroidradar.data.api

import com.udacity.asteroidradar.data.model.dto.PictureOfDayDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// ?start_date=START_DATE&end_date=END_DATE&api_key=API_KEY

interface NasaApi {

    @GET("neo/rest/v1/feed")
    fun nasaApiCall(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): Call<String>


    @GET("planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String
    ): Response<PictureOfDayDto>
}