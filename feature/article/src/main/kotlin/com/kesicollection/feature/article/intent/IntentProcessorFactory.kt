package com.kesicollection.feature.article.intent

import com.kesicollection.data.usecase.GetArticleByIdUseCase
import com.kesicollection.feature.article.Intent
import javax.inject.Inject
import javax.inject.Singleton

interface IntentProcessorFactory {
    fun create(intent: Intent): IntentProcessor
}

@Singleton
class DefaultIntentProcessorFactory @Inject constructor(
    private val getArticleByIdUseCase: GetArticleByIdUseCase,
) : IntentProcessorFactory {
    override fun create(intent: Intent): IntentProcessor {
        return when (intent) {
            is Intent.FetchArticle -> FetchArticleIntentProcessor(
                articleId = intent.id,
                getArticleByIdUseCase = getArticleByIdUseCase
            )
        }
    }
}