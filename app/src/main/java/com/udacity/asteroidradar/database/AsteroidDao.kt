package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Query("select * from tbl_asteroid order by closeApproachDate ASC")
    fun getAsteroidList(): LiveData<List<DbAsteroid>>

    @Query("Select * from tbl_asteroid where closeApproachDate >= :date order by closeApproachDate ASC")
    fun getFutureAsteroidList(date: String): LiveData<List<DbAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroidList: List<DbAsteroid>)
}