package com.kesicollection.articles

import com.kesicollection.articles.model.UiArticle
import com.kesicollection.core.uisystem.ErrorState

val initialState = UiArticlesState()

/**
 * Represents the UI state for the articles screen.
 *
 * @property articles The list of articles to be displayed. Defaults to an empty list.
 * @property isLoading Indicates whether the articles are currently being loaded. Defaults to false.
 * @property screenError Represents an error state that might occur during loading. Null if no error.
 */
data class UiArticlesState(
    val articles: List<UiArticle> = emptyList(),
    val isLoading: Boolean = false,
    val screenError: ErrorState<ArticlesErrors>? = null
)

/**
 * Represents the possible errors that can occur in the articles feature.
 *
 * @property NetworkCallError Indicates an error occurred during a network call.
 */
enum class ArticlesErrors {
    NetworkCallError
}

/**
 * Represents the user's intent or action within the application.
 *
 * This interface is used to define the various actions that a user can perform.
 * Each data object represents a specific user interaction that can trigger state changes.
 */
sealed interface Intent {
    data object FetchArticles : Intent
}

