package com.kesicollection.feature.article.uimodel

/**
 * Represents a podcast in the user interface.
 *
 * @property id The unique identifier of the podcast.
 * @property title The title of the podcast.
 * @property audioUrl The URL to the audio file of the podcast.
 */
data class UiPodcast(
    val id: String,
    val title: String,
    val audioUrl: String
)
