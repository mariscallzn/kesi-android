package com.kesicollection.articles.intentprocessor

import com.kesicollection.articles.Intent
import com.kesicollection.articles.UiArticlesState
import com.kesicollection.core.uisystem.IntentProcessor
import com.kesicollection.core.uisystem.IntentProcessorFactory
import com.kesicollection.data.usecase.GetArticlesUseCase
import javax.inject.Inject

/**
 * A factory class responsible for creating [IntentProcessor] instances based on the provided [Intent].
 *
 * This implementation currently creates lightweight [IntentProcessor] objects on each request.
 * Alternative strategies, such as caching, could be implemented for performance optimization in the future.
 *
 * @property getArticlesUseCase A [GetArticlesUseCase] instance used by [FetchArticlesIntentProcessor].
 */
class DefaultIntentProcessorFactory @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
) : IntentProcessorFactory<UiArticlesState, Intent> {

    // NOTE: Two options here: Either make some how lightweight intent processor instances
    // or build some cache mechanism to not create new instances everytime they are requested.
    // for now we will go with lightweight objects since the heavy object will be injected
    // to the DefaultsIntentProcessorFactory as singletons.
    override fun create(intent: Intent): IntentProcessor<UiArticlesState> {
        return when (intent) {
            Intent.FetchArticles -> FetchArticlesIntentProcessor(getArticlesUseCase = getArticlesUseCase)
        }
    }
}