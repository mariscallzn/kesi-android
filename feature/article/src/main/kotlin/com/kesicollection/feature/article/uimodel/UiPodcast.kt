package com.kesicollection.feature.article.uimodel

import com.kesicollection.core.model.Podcast

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

/**
 * Converts a [Podcast] domain model to a [UiPodcast] for UI representation.
 *
 * @receiver The [Podcast] domain model to convert.
 * @return A [UiPodcast] containing the podcast's ID, title, and audio URL.
 */
fun Podcast.asUiPodcast() = UiPodcast(
    id = id,
    title = title,
    audioUrl = url
)
