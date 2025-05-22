package com.kesicollection.articles.intentprocessor

import com.kesicollection.articles.Intent
import com.kesicollection.articles.UiArticlesState
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.data.usecase.BookmarkArticleByIdUseCase
import com.kesicollection.data.usecase.GetArticlesUseCase
import com.kesicollection.data.usecase.IsArticleBookmarkedUseCase
import javax.inject.Inject

/**
 * The `DefaultIntentProcessorFactory` is a concrete implementation of the [IntentProcessorFactory]
 * interface. It's responsible for creating and providing [IntentProcessor] instances tailored to
 * specific [Intent] types.
 *
 * This factory currently adopts a lightweight strategy by creating new [IntentProcessor] objects
 * upon each request. However, it's designed with future flexibility in mind, allowing for the
 * potential integration of caching or other performance enhancement strategies if needed.
 *
 * @property getArticlesUseCase A [GetArticlesUseCase] instance used by [FetchArticlesIntentProcessor].
 */
class DefaultIntentProcessorFactory @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val isArticleBookmarkedUseCase: IsArticleBookmarkedUseCase,
    private val bookmarkArticleByIdUseCase: BookmarkArticleByIdUseCase,
    private val crashlyticsWrapper: CrashlyticsWrapper,
) : IntentProcessorFactory<UiArticlesState, Intent> {

    // NOTE: Two options here: Either make some how lightweight intent processor instances
    // or build some cache mechanism to not create new instances everytime they are requested.
    // for now we will go with lightweight objects since the heavy object will be injected
    // to the DefaultsIntentProcessorFactory as singletons.
    override fun create(intent: Intent): IntentProcessor<UiArticlesState> {
        return when (intent) {
            Intent.FetchArticles -> FetchArticlesIntentProcessor(
                getArticlesUseCase = getArticlesUseCase,
                isArticleBookmarkedUseCase = isArticleBookmarkedUseCase,
                crashlyticsWrapper = crashlyticsWrapper,
            )

            is Intent.BookmarkClicked -> BookmarkArticleIntentProcessor(
                articleId = intent.articleId,
                bookmarkArticleByIdUseCase = bookmarkArticleByIdUseCase,
                getArticlesUseCase = getArticlesUseCase,
                isArticleBookmarkedUseCase = isArticleBookmarkedUseCase,
                crashlyticsWrapper = crashlyticsWrapper,
            )
        }
    }
}