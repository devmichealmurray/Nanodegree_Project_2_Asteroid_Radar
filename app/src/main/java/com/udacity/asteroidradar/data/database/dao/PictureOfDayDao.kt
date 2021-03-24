package com.udacity.asteroidradar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.model.entity.PictureOfDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureOfDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPicture(picture: PictureOfDayEntity)

    @Query("SELECT * FROM picture_of_the_day")
    fun getPicture(): Flow<PictureOfDayEntity>

    @Query("DELETE FROM picture_of_the_day")
    suspend fun deletePicture()
}