package com.kesicollection.core.model

/**
 * Represents an article with its essential information.
 *
 * @property id The unique identifier of the article.
 * @property title The title of the article.
 * @property description A brief description or summary of the article's content.
 * @property thumbnail The URL or path to the article's thumbnail image.
 */
data class Article(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String
)
