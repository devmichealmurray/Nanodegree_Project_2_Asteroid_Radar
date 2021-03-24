package com.udacity.asteroidradar.data.model.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PictureOfDayDto(
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "title") val title: String,
    @Json(name = "url") val url: String
)