package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.DbPictureOfTheDay
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.PictureOfTheDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    //val pictureOfDay = MutableLiveData<DbPictureOfTheDay>()
    val pictureOfDayUrl = MutableLiveData<String>()
    //val pictureOfDayUrl = MutableLiveData<String>(pictureOfDay?.value?.url)

    init {
        viewModelScope.launch {
            // Fetch Pic of Day
            asteroidRepository.refreshPictureOfTheDay()

            //pictureOfDay.value = asteroidRepository.pictureOfTheDay.value

            // Fetch Asteroid info from server or db
             //asteroidRepository.refreshAsteroids()
        }
    }

    // Get info from repository
    val asteroidList = asteroidRepository.asteroidList
    val pictureOfDay = asteroidRepository.pictureOfTheDay
    //val pictureOfDayUrl = MutableLiveData<String>(pictureOfDay?.value?.url)

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