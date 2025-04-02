package com.kesicollection.feature.article

import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.component.ContentType

val initialState = UiArticleState()

data class UiArticleState(
    val isLoading: Boolean = false,
    val title: String = "",
    val imageUrl: String = "",
    val content: List<ContentType> = emptyList(),
    val error: ErrorState<ArticleErrors>? = null,
)

enum class ArticleErrors {
    NetworkError,
}

sealed interface Intent {
    data class FetchArticle(val id: String) : Intent
}

typealias Reducer = UiArticleState.() -> UiArticleState
