package com.kesicollection.articles.intentprocessor

import com.kesicollection.articles.ArticlesErrors
import com.kesicollection.articles.UiArticlesState
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.Reducer
import com.kesicollection.domain.BookmarkArticleByIdUseCase
import com.kesicollection.domain.GetArticlesUseCase
import com.kesicollection.domain.IsArticleBookmarkedUseCase

/**
 * [BookmarkArticleIntentProcessor] is an [IntentProcessor] responsible for handling the
 * intent of bookmarking or un-bookmarking a specific article.
 *
 * It interacts with several use cases to perform the operation and update the UI state accordingly.
 *
 * @property articleId The ID of the article to be bookmarked or un-bookmarked.
 * @property bookmarkArticleByIdUseCase Use case responsible for bookmarking an article by its ID.
 * @property isArticleBookmarkedUseCase Use case responsible for checking if an article is bookmarked.
 * @property getArticlesUseCase Use case responsible for retrieving the list of articles.
 */
class BookmarkArticleIntentProcessor(
    private val articleId: String,
    private val bookmarkArticleByIdUseCase: BookmarkArticleByIdUseCase,
    private val isArticleBookmarkedUseCase: IsArticleBookmarkedUseCase,
    private val getArticlesUseCase: GetArticlesUseCase,
    private val crashlyticsWrapper: CrashlyticsWrapper,
) : IntentProcessor<UiArticlesState> {
    override suspend fun processIntent(reducer: (Reducer<UiArticlesState>) -> Unit) {
        reducer {
            copy(articles = articles.map {
                if (it.articleId == articleId) it.copy(isBookmarked = false) else it
            })
        }
        try {
            bookmarkArticleByIdUseCase(articleId)
            val articles = getArticlesUseCase().getOrThrow().map {
                it.asUiArticle().copy(isBookmarked = isArticleBookmarkedUseCase(it.id))
            }
            reducer {
                copy(articles = articles)
            }
        } catch (e: Exception) {
            crashlyticsWrapper.recordException(
                e, mapOf(
                    crashlyticsWrapper.params.screenName to "Articles",
                    crashlyticsWrapper.params.className to "BookmarkArticleIntentProcessor",
                    crashlyticsWrapper.params.action to "bookmark",
                )
            )
            reducer {
                copy(
                    isLoading = false,
                    error = ErrorState(ArticlesErrors.GenericError, e.message)
                )
            }
        }
    }
}