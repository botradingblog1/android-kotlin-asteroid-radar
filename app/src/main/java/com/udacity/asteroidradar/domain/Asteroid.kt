package com.udacity.asteroidradar.domain

import android.os.Parcelable
import com.udacity.asteroidradar.database.DbAsteroid
import kotlinx.android.parcel.Parcelize

// Business layer object
@Parcelize
data class Asteroid(val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable

/**
 * Convert the list of domain Asteroid entities into the database model
 */
fun List<Asteroid>.asDatabaseModel(): List<DbAsteroid> {
    return map {
        DbAsteroid (
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}