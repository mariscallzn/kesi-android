package com.kesicollection.feature.article

import com.kesicollection.core.uisystem.ErrorState
import com.kesicollection.core.uisystem.component.ContentType
import com.kesicollection.feature.article.uimodel.UiPodcast

val initialState = UiArticleState()

data class UiArticleState(
    val isLoading: Boolean = false,
    val title: String = "",
    val imageUrl: String = "",
    val podcast: UiPodcast? = null,
    val content: List<ContentType> = emptyList(),
    val error: ErrorState<ArticleErrors>? = null,
)

enum class ArticleErrors {
    NetworkError,
}

sealed interface Intent {
    data class FetchArticle(val id: String) : Intent
}
