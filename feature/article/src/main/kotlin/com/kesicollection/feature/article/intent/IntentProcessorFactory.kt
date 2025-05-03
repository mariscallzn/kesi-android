package com.kesicollection.feature.article.intent

import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.uisystem.IntentProcessor
import com.kesicollection.core.uisystem.IntentProcessorFactory
import com.kesicollection.data.usecase.GetArticleByIdUseCase
import com.kesicollection.feature.article.Intent
import com.kesicollection.feature.article.UiArticleState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A concrete implementation of [IntentProcessorFactory] that creates [IntentProcessor] instances
 * based on the provided [Intent].
 *
 * This factory is responsible for mapping different [Intent] types to their corresponding
 * [IntentProcessor] implementations. Currently, it supports creating a [FetchArticleIntentProcessor]
 * for [Intent.FetchArticle] intents.
 *
 * @property getArticleByIdUseCase The use case for retrieving an article by its ID.
 *   This is used by the [FetchArticleIntentProcessor] to fetch article data.
 */
@Singleton
class DefaultIntentProcessorFactory @Inject constructor(
    private val getArticleByIdUseCase: GetArticleByIdUseCase,
    private val crashlyticsWrapper: CrashlyticsWrapper,
) : IntentProcessorFactory<UiArticleState, Intent> {
    override fun create(intent: Intent): IntentProcessor<UiArticleState> {
        return when (intent) {
            is Intent.FetchArticle -> FetchArticleIntentProcessor(
                articleId = intent.id,
                getArticleByIdUseCase = getArticleByIdUseCase,
                crashlyticsWrapper = crashlyticsWrapper,
            )
        }
    }
}