package com.udacity.asteroidradar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.model.entity.AsteroidEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAsteroid(asteroid: AsteroidEntity)

    @Query("SELECT * FROM asteroids")
    fun getAsteroids(): Flow<List<AsteroidEntity>>

    @Query("DELETE FROM asteroids")
    suspend fun deleteAllAsteroids()

    @Query("DELETE FROM asteroids WHERE uid = :id")
    suspend fun deleteAsteroidById(id: Long)
}