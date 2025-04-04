package com.kesicollection.articles.intentprocessor

import com.kesicollection.articles.IntentProcessor
import com.kesicollection.articles.Reducer
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.data.usecase.GetArticlesUseCase
import kotlinx.coroutines.delay

class FetchArticlesIntentProcessor(
    private val getArticlesUseCase: GetArticlesUseCase,
) : IntentProcessor {
    override suspend fun processIntent(reducer: (Reducer) -> Unit) {
        reducer { copy(isLoading = true) }
        val articles = getArticlesUseCase().getOrThrow()
        reducer {
            copy(
                isLoading = false,
                articles = articles.map { it.asUiArticle() }
            )
        }
    }
}