package com.kesicollection.articles.intentprocessor

import com.kesicollection.articles.ArticlesErrors
import com.kesicollection.articles.UiArticlesState
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.uisystem.ErrorState
import com.kesicollection.core.uisystem.IntentProcessor
import com.kesicollection.core.uisystem.Reducer
import com.kesicollection.data.usecase.GetArticlesUseCase
import com.kesicollection.data.usecase.IsArticleBookmarkedUseCase

/**
 * [IntentProcessor] responsible for fetching and preparing articles for display in the UI.
 *
 * This processor orchestrates the retrieval of articles from the data layer via [GetArticlesUseCase],
 * determines the bookmarked status of each article using [IsArticleBookmarkedUseCase],
 * and updates the UI state (represented by [UiArticlesState]) accordingly.
 *
 * **Core Responsibilities:**
 *
 * 1. **Initiates Loading State:** Sets the `isLoading` flag in [UiArticlesState] to `true` before fetching articles,
 *    indicating to the UI that data is being loaded.
 * 2. **Retrieves Articles:**  Utilizes the provided [getArticlesUseCase] to fetch a list of articles.
 * 3. **Checks Bookmark Status:** For each fetched article, it uses [isArticleBookmarkedUseCase] to determine
 *    if the article has been bookmarked by the user.
 * 4. **Transforms to UI Model:** Maps the fetched articles to UI-ready models using the `asUiArticle()` function.
 *    It also merges the bookmark status obtained from  [isArticleBookmarkedUseCase] into each UI article model.
 * 5. **Updates UI State (Success):** Upon successful retrieval, it updates the UI state by:
 *    - Setting `isLoading` to `false`.
 *    - Populating the `articles` list with the transformed UI models.
 *    - Clearing any existing error state (`error = null`).
 * 6. **Handles Errors:** If an exception occurs during the fetching process, it handles the error by:
 *    - Setting `isLoading` to `false`.
 *    - Setting `articles` to an empty list.
 *    - Setting the `error` field in [UiArticlesState] to an [ErrorState] containing
 *      [ArticlesErrors.NetworkError] and the error message.
 *
 * **Key Dependencies:**
 *
 * - **[getArticlesUseCase]:**  The use case responsible for retrieving articles from the data layer.
 * - **[isArticleBookmarkedUseCase]:** The use case responsible for checking if an article is bookmarked.
 *
 * **Usage:**
 *
 * This class is designed to be used within an architecture that utilizes [IntentProcessor] and [Reducer] to manage
 */
class FetchArticlesIntentProcessor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val isArticleBookmarkedUseCase: IsArticleBookmarkedUseCase,
    private val crashlyticsWrapper: CrashlyticsWrapper,
) : IntentProcessor<UiArticlesState> {
    override suspend fun processIntent(reducer: (Reducer<UiArticlesState>) -> Unit) {
        reducer { copy(isLoading = true, error = null) }
        try {
            val articles = getArticlesUseCase().getOrThrow().map {
                it.asUiArticle().copy(isBookmarked = isArticleBookmarkedUseCase(it.id))
            }
            reducer {
                copy(
                    isLoading = false,
                    articles = articles
                )
            }
        } catch (e: Exception) {
            crashlyticsWrapper.recordException(
                exception = e,
                params = mapOf(
                    crashlyticsWrapper.params.screenName to "Articles",
                    crashlyticsWrapper.params.className to "FetchArticlesIntentProcessor",
                    crashlyticsWrapper.params.action to "fetch",
                )
            )
            reducer {
                copy(
                    isLoading = false,
                    articles = emptyList(),
                    error = ErrorState(ArticlesErrors.NetworkError, e.message)
                )
            }
        }
    }
}