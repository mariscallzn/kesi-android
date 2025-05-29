package com.kesicollection.core.model

/**
 * Represents an article with its essential information.
 *
 * @property id The unique identifier of the article.
 * @property title The title of the article.
 * @property description A brief description or summary of the article's content.
 * @property markdown The full content of the article in Markdown format.
 * @property img The URL or path to the article's thumbnail image.
 * @property podcast The associated [Podcast] if any.
 */
data class Article(
    val id: String,
    val title: String,
    val description: String,
    val img: String,
    val markdown: String,
    val podcast: Podcast?,
)