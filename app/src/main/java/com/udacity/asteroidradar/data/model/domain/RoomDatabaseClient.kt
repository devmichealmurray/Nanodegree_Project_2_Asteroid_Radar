package com.udacity.asteroidradar.data.model.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.data.database.dao.AsteroidDao
import com.udacity.asteroidradar.data.database.dao.PictureOfDayDao
import com.udacity.asteroidradar.data.model.entity.AsteroidEntity
import com.udacity.asteroidradar.data.model.entity.PictureOfDayEntity


const val DATABASE_SCHEMA_VERSION = 3
const val DATABASE_NAME = "nasa_db"

@Database(
    version = DATABASE_SCHEMA_VERSION,
    entities = [
        AsteroidEntity::class,
        PictureOfDayEntity::class
    ],
    exportSchema = false
)
abstract class RoomDatabaseClient : RoomDatabase() {

    abstract fun asteroidDao() : AsteroidDao
    abstract fun pictureOfDayDao() : PictureOfDayDao

    companion object {
        private var instance: RoomDatabaseClient? = null

        private fun createDatabase(context: Context): RoomDatabaseClient {
            return Room
                .databaseBuilder(context, RoomDatabaseClient::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        fun getDbInstance(context: Context): RoomDatabaseClient =
            (instance ?: createDatabase(context)).also {
                instance = it
            }
    }
}