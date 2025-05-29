package com.kesicollection.feature.article.intent

import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.domain.GetArticleByIdUseCase
import com.kesicollection.domain.GetMarkdownAsString
import com.kesicollection.feature.article.Intent
import com.kesicollection.feature.article.UiArticleState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A concrete implementation of [IntentProcessorFactory] that creates [IntentProcessor] instances
 * based on the provided [Intent].
 *
 * This factory is responsible for mapping different [Intent] types to their corresponding
 * [IntentProcessor] implementations. Currently, it supports the creation of:
 * - [FetchArticleIntentProcessor] for [Intent.FetchArticle] intents.
 *
 * @property getMarkdownAsString The use case for retrieving markdown content as a string.
 * @property getArticleByIdUseCase The use case for retrieving an article by its ID.
 * @property crashlyticsWrapper A wrapper for Crashlytics to report errors.
 */
@Singleton
class DefaultIntentProcessorFactory @Inject constructor(
    private val getMarkdownAsString: GetMarkdownAsString,
    private val getArticleByIdUseCase: GetArticleByIdUseCase,
    private val crashlyticsWrapper: CrashlyticsWrapper,
) : IntentProcessorFactory<UiArticleState, Intent> {
    override fun create(intent: Intent): IntentProcessor<UiArticleState> {
        return when (intent) {
            is Intent.FetchArticle -> FetchArticleIntentProcessor(
                articleId = intent.id,
                getMarkdownAsString = getMarkdownAsString,
                getArticleByIdUseCase = getArticleByIdUseCase,
                crashlyticsWrapper = crashlyticsWrapper,
            )
        }
    }
}