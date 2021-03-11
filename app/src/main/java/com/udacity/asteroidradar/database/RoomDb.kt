package com.udacity.asteroidradar.database


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


// Database definitions
@Database(entities = [DbAsteroid::class, DbPictureOfTheDay::class], version = 2, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfTheDayDao: PictureOfTheDayDao
}

// Global Room database instance
private lateinit var INSTANCE: AsteroidDatabase
fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroid_db")
                .fallbackToDestructiveMigration().build()
        }

        return INSTANCE
    }
}
