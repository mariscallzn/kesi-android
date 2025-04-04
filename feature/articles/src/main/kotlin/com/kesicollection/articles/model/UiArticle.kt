package com.kesicollection.articles.model

import com.kesicollection.core.model.Article

/**
 * Represents an article in the UI layer.
 *
 * @property articleId The unique identifier of the article.
 * @property title The title of the article.
 * @property description A brief description of the article's content.
 * @property thumbnail The URL of the article's thumbnail image.
 */
data class UiArticle(
    val articleId: String,
    val title: String,
    val description: String,
    val thumbnail: String
)

/**
 * Converts an [Article] domain model to a [UiArticle] for presentation in the UI.
 *
 * @receiver The [Article] instance to convert.
 * @return A [UiArticle] instance containing the relevant data for display.
 */
fun Article.asUiArticle() = UiArticle(
    articleId = id,
    title = title,
    description = description,
    thumbnail = thumbnail
)
