package com.udacity.asteroidradar.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.data.model.domain.Asteroid

@Entity(tableName = "asteroids")
class AsteroidEntity(
    @PrimaryKey(autoGenerate = false)
    var uid: Long = -1L,
    @ColumnInfo(name = "code_name")
    val codeName: String?,
    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String?,
    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double?,
    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double?,
    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double?,
    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double?,
    @ColumnInfo(name = "is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean?,
    @ColumnInfo(name = "date_millis")
    val dateMillis: Long
) {
    fun toAsteroid() = Asteroid(
        uid,
        codeName,
        closeApproachDate,
        absoluteMagnitude,
        estimatedDiameter,
        relativeVelocity,
        distanceFromEarth,
        isPotentiallyHazardous,
        dateMillis
    )
}