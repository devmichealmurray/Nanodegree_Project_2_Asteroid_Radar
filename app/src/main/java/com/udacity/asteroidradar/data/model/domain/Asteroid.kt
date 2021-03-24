package com.udacity.asteroidradar.data.model.domain

data class Asteroid(
    val id: Long?,
    val codename: String?,
    val closeApproachDate: String?,
    val absoluteMagnitude: Double?,
    val estimatedDiameter: Double?,
    val relativeVelocity: Double?,
    val distanceFromEarth: Double?,
    val isPotentiallyHazardous: Boolean?,
    val date: Long?
)