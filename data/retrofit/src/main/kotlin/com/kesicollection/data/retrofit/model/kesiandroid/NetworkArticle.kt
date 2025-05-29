package com.kesicollection.data.retrofit.model.kesiandroid

import com.kesicollection.core.model.Article
import com.kesicollection.core.model.Podcast
import kotlinx.serialization.Serializable

/**
 * Represents a summary of an article fetched from the network, typically for display in a list or index.
 *
 * @property id The unique identifier of the article.
 * @property title The title of the article.
 * @property description A short description of the article.
 * @property img The URL of the article's thumbnail image.
 */
@Serializable
data class NetworkIndexArticle(
    val id: String,
    val title: String,
    val description: String,
    val img: String,
)

/**
 * Represents an article fetched from the network.
 *
 * @property id The unique identifier of the article.
 * @property title The title of the article.
 * @property description A short description of the article.
 * @property img The URL of the article's thumbnail image.
 * @property markdown The markdown content of the article.
 * @property podcast Optional data about a podcast related to this article.
 */
@Serializable
data class NetworkArticle(
    val id: String,
    val title: String,
    val description: String,
    val img: String,
    val markdown: String,
    val podcast: NetworkPodcast?,
)

/**
 * Converts a [NetworkIndexArticle] to an [Article].
 *
 * This function maps the properties of a [NetworkIndexArticle] to an [Article].
 * Since [NetworkIndexArticle] represents a summary, the `markdown` property is set to an empty string
 * and the `podcast` property is set to `null`.
 *
 * @return An [Article] object created from the [NetworkIndexArticle].
 */
fun NetworkIndexArticle.asArticle() = Article(
    id = id,
    title = title,
    description = description,
    img = img,
    markdown = "",
    podcast = null,
)

/**
 * Converts a [NetworkArticle] to an [Article].
 *
 * This function maps the properties of a [NetworkArticle] object to an [Article] object.
 * If the [NetworkArticle] has an associated [NetworkPodcast], it is also converted to a [Podcast]
 * object using the [NetworkPodcast.asPodcast] extension function.
 *
 * @return An [Article] object created from the [NetworkArticle].
 */
fun NetworkArticle.asArticle() = Article(
    id = id,
    title = title,
    description = description,
    img = img,
    markdown = markdown,
    podcast = podcast?.asPodcast()
)