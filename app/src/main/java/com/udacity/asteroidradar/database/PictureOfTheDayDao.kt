package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PictureOfTheDayDao {
    @Query("select * from tbl_picture_of_the_day limit 1")
    fun getPictureOfTheDay(): LiveData<DbPictureOfTheDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pictureOfTheDay: DbPictureOfTheDay)

    @Query("delete from tbl_picture_of_the_day")
    fun deleteAll()
}