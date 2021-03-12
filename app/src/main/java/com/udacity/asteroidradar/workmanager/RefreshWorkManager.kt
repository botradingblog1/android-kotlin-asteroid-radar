package com.udacity.asteroidradar.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshWorkManager(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            // Fetch NEO info from server
            repository.refreshAsteroids()
            repository.refreshPictureOfTheDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshWorkManager"
    }
}