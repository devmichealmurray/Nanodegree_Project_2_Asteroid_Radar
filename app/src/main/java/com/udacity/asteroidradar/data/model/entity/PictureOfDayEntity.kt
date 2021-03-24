package com.udacity.asteroidradar.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.data.model.domain.PictureOfDay

@Entity(tableName = "picture_of_the_day")
data class PictureOfDayEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0L,
    @ColumnInfo(name = "date_millis")
    val dateMillis: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "media_type")
    val mediaType: String?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "url")
    val url: String?
) {
    fun toPictureOfDay() = PictureOfDay(
        mediaType = mediaType,
        title = title,
        url = url
    )
}