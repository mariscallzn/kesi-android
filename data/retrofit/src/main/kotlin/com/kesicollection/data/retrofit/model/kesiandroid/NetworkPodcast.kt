package com.kesicollection.data.retrofit.model.kesiandroid

import com.kesicollection.core.model.Podcast
import kotlinx.serialization.Serializable

/**
 * Represents a podcast data model received from the network.
 *
 * @property id The unique identifier of the podcast.
 * @property title The title of the podcast.
 * @property url The URL where the podcast's RSS feed can be found.
 * @property image The URL of the image representing the podcast.
 */
@Serializable
data class NetworkPodcast(
    val id: String,
    val title: String,
    val url: String,
    val image: String,
)

/**
 * Converts a [NetworkPodcast] object to a [Podcast] object.
 *
 * This function maps the properties from the network representation ([NetworkPodcast])
 * to the domain representation ([Podcast]).
 *
 * @return A [Podcast] object with the same id, title, url, and image as this [NetworkPodcast].
 */
fun NetworkPodcast.asPodcast() = Podcast(
    id = id,
    title = title,
    url = url,
    image = image
)