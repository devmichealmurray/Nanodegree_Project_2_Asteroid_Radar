package com.udacity.asteroidradar.data.repository

import com.udacity.asteroidradar.data.database.dao.AsteroidDao
import com.udacity.asteroidradar.data.database.dao.PictureOfDayDao
import com.udacity.asteroidradar.data.model.entity.AsteroidEntity
import com.udacity.asteroidradar.data.model.entity.PictureOfDayEntity

class DatabaseRepository(
    private val asteroidDataSource: AsteroidDao,
    private val photoDataSource: PictureOfDayDao
) {

    // Asteroid Functions

    suspend fun addAsteroid(asteroid: AsteroidEntity) =
        asteroidDataSource.addAsteroid(asteroid)

    fun getAsteroids() =
        asteroidDataSource.getAsteroids()

    suspend fun deleteAllAsteroids() =
        asteroidDataSource.deleteAllAsteroids()

    suspend fun deleteAsteroidById(id: Long) =
        asteroidDataSource.deleteAsteroidById(id)



    // Picture Of The Day Functions

    suspend fun addPicture(picture: PictureOfDayEntity) =
        photoDataSource.addPicture(picture)

    fun getPicture() =
        photoDataSource.getPicture()

    suspend fun deletePicture() =
        photoDataSource.deletePicture()
}