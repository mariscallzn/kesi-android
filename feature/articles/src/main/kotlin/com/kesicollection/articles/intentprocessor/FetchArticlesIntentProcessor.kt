package com.kesicollection.articles.intentprocessor

import com.kesicollection.articles.ArticlesErrors
import com.kesicollection.articles.IntentProcessor
import com.kesicollection.articles.Reducer
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.core.uisystem.ErrorState
import com.kesicollection.data.usecase.GetArticlesUseCase
import kotlinx.coroutines.delay


/**
 * [IntentProcessor] responsible for fetching articles from the [GetArticlesUseCase] and updating the UI state.
 *
 * This processor performs the following actions:
 * 1. Sets the loading state to `true`.
 * 2. Fetches articles using the provided [getArticlesUseCase].
 * 3. If successful, maps the fetched articles to UI models using [asUiArticle] and updates the UI state with the list of articles, setting the loading state to `false`.
 * 4. If an error occurs during fetching, sets the loading state to `false`, sets the articles to an empty list, and sets the `screenError` with the [ArticlesErrors.NetworkCallError] error state.
 *
 * @property getArticlesUseCase The use case responsible for retrieving articles.
 */
class FetchArticlesIntentProcessor(
    private val getArticlesUseCase: GetArticlesUseCase,
) : IntentProcessor {
    override suspend fun processIntent(reducer: (Reducer) -> Unit) {
        reducer { copy(isLoading = true, screenError = null) }
        try {
            delay(1500)
            throw IllegalStateException("test")
            val articles = getArticlesUseCase().getOrThrow()
            reducer {
                copy(
                    isLoading = false,
                    articles = articles.map { it.asUiArticle() }
                )
            }
        } catch (e: Exception) {
            reducer {
                copy(
                    isLoading = false,
                    articles = emptyList(),
                    screenError = ErrorState(ArticlesErrors.NetworkCallError, e.message)
                )
            }
        }
    }
}