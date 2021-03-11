package com.udacity.asteroidradar.ui

import com.udacity.asteroidradar.domain.Asteroid

/**
 * Click listener for Asteroid card.
 *
 */
class AsteroidClickListener(val block: (Asteroid) -> Unit) {
    /**
     * Called when an Asteroid card is clicked
     *
     * @param asteroid: the obj associated with the card
     */
    fun onClick(asteroid: Asteroid) = block(asteroid)
}