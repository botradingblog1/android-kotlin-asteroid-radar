package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfTheDay

/**
 * DbPictureOfTheDay is the picture of the day model for the Room db
 */
@Entity(tableName = "tbl_picture_of_the_day")
data class DbPictureOfTheDay constructor(
    @PrimaryKey
    val id: Long,
    val title: String,
    val url: String,
    val mediaType: String)

/**
 * Convert the db picture of the day entities into the domain model
 */
fun DbPictureOfTheDay.asDomainModel(): PictureOfTheDay {
    return PictureOfTheDay (
            this.id,
            this.title,
            this.url,
            this.mediaType
        )

}
