package com.udacity.asteroidradar.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.asDatabaseModel
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parsePictureOfTheDay
import com.udacity.asteroidradar.database.DbPictureOfTheDay
import com.udacity.asteroidradar.domain.PictureOfTheDay
import com.udacity.asteroidradar.support.Tuple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


// Repository to either fetch Asteroid info from server or from database
class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroidList: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroidList()) {
        it.asDomainModel()
    }

    val pictureOfTheDay: LiveData<DbPictureOfTheDay> = database.pictureOfTheDayDao.getPictureOfTheDay()

    private fun getQueryDates(): Tuple<String, String> {
        val startDate = Date()
        var formatter = SimpleDateFormat("yyyy-MM-dd")
        val startDateStr = formatter.format(startDate)
        val calendar = Calendar.getInstance() // Get Calendar Instance
        calendar.time = formatter.parse(startDateStr)

        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)

        // Format end date
        val endDate = Date(calendar.getTimeInMillis())
        val endDateStr = formatter.format(endDate)

        return Tuple(startDateStr, endDateStr)
    }

    // Fetch Asteroid Info from NASA API and insert them into the database
    suspend fun refreshAsteroids() {
        val dates = getQueryDates()

        val response = Network.asteroidService.getAsteroidList(
            dates._1,
            dates._2,
            BuildConfig.API_KEY
        )

        if (response.isSuccessful && response.body() != null) {
            try {
                val jsonObject = JSONObject(response.body())

                // Convert JSON to domain objects
                val asteroidList = parseAsteroidsJsonResult(jsonObject)
                Timber.d("NASA Get Asteroids API returned %d records",asteroidList.size)

                // Convert to database objects
                val dbAsteroidList = asteroidList.asDatabaseModel()

                // Insert records into db
                database.asteroidDao.insertAll(dbAsteroidList)

                Timber.d("Refresh Asteroid info complete")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            Timber.e("getAsteroid call not successful: %s", response.errorBody().toString())
        }
    }

    // Fetch Picture of the day from NASA API and insert them into the database
    suspend fun refreshPictureOfTheDay() {
        val response = Network.asteroidService.getPictureOfTheDay(
            BuildConfig.API_KEY
        )

        if (response.isSuccessful && response.body() != null) {
            try {
                val jsonObject = JSONObject(response.body())

                // Convert JSON to domain objects
                val dbPictureOfDay = parsePictureOfTheDay(jsonObject)
                Timber.d("NASA Get Pic of Day API returned: %s",dbPictureOfDay.url)

                // Insert records into db
                database.pictureOfTheDayDao.insert(dbPictureOfDay)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            Timber.e("Get pic of day call not successful: %s", response.errorBody().toString())
        }
    }
}