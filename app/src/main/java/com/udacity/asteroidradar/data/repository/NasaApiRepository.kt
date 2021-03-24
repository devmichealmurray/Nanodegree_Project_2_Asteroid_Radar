package com.udacity.asteroidradar.data.repository

import com.udacity.asteroidradar.data.api.NasaApiService
import com.udacity.asteroidradar.data.api.NasaPictureApiService
import com.udacity.asteroidradar.data.model.dto.PictureOfDayDto
import retrofit2.Call
import retrofit2.Response

private const val API_KEY = "ErHNqQmYRrD8rIjUI2a86R8j2igPtDKN920nMhLM"

object NasaApiRepository {

    fun getAsteroids(
        startDate: String,
        endDate: String
    ) : Call<String> {
        return NasaApiService.apiClient.nasaApiCall(
            startDate,
            endDate,
            apiKey = API_KEY
        )
    }

    suspend fun getPictureOfTheDay() : Response<PictureOfDayDto> {
        return NasaPictureApiService.apiClient.getPictureOfDay(apiKey = API_KEY)
    }
}