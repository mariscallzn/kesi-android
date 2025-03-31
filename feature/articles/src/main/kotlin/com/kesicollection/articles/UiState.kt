package com.kesicollection.articles

import com.kesicollection.articles.model.UiArticle
import com.kesicollection.core.model.ErrorState

val initialState = UiArticlesState()

data class UiArticlesState(
    val articles: List<UiArticle> = emptyList(),
    val isLoading: Boolean = false,
    val screenError: ErrorState<ArticlesErrors>? = null
)

enum class ArticlesErrors {
    NetworkCallError
}

typealias Reducer = UiArticlesState.() -> UiArticlesState

sealed interface Intent {
    data object FetchArticles : Intent
}

interface IntentProcessor {
    suspend fun processIntent(reducer: (Reducer) -> Unit)
}

