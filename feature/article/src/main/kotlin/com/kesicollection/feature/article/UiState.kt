package com.kesicollection.feature.article

import com.kesicollection.core.model.ErrorState

val initialState = UiArticleState()

data class UiArticleState(
    val isLoading: Boolean = false,
    val tmpText: String = "",
    val error: ErrorState<ArticleErrors>? = null,
)

enum class ArticleErrors {
    NetworkError,
}

sealed interface Intent {
    data class FetchArticle(val id: String) : Intent
}

typealias Reducer = UiArticleState.() -> UiArticleState
