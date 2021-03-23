package com.udacity.asteroidradar.data.model.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AsteroidDto(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)