package com.udacity.asteroidradar.data.repository

import com.udacity.asteroidradar.data.api.NasaApiService
import com.udacity.asteroidradar.data.model.dto.AsteroidDto
import retrofit2.Response

private const val API_KEY = "ErHNqQmYRrD8rIjUI2a86R8j2igPtDKN920nMhLM"

object NasaApiRepository {

    suspend fun getAsteroids(
        startDate: String,
        endDate: String
    ) : Response<AsteroidDto> {
        return NasaApiService.apiClient.NasaApiCall(
            startDate,
            endDate,
            apiKey = API_KEY
        )
    }
}