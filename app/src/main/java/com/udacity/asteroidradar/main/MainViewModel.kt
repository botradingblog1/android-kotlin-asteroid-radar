package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.DbPictureOfTheDay
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.PictureOfTheDay
import com.udacity.asteroidradar.network.NetworkConnectivityChecker
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            val isNetworkAvailable = NetworkConnectivityChecker.isNetworkAvailable(application)
            if (isNetworkAvailable) {
                // Fetch Pic of Day
                asteroidRepository.refreshPictureOfTheDay()

                // Refresh Asteroid info
                asteroidRepository.refreshAsteroids()
            }
        }
    }

    // Get info from repository
    val asteroidList = asteroidRepository.asteroidList
    val pictureOfDay = asteroidRepository.pictureOfTheDay

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}