package com.kesicollection.articles.intentprocessor

import com.kesicollection.articles.Intent
import com.kesicollection.articles.IntentProcessor
import javax.inject.Inject

interface IntentProcessorFactory {
    fun create(intent: Intent): IntentProcessor
}

class DefaultIntentProcessorFactory @Inject constructor(
    //todo: pass down use cases so you can pass it to the intent processors instances.
) : IntentProcessorFactory {

    // NOTE: Two options here: Either make some how lightweight intent processor instances
    // or build some cache mechanism to not create new instances everytime they are requested.
    // for now we will go with lightweight objects since the heavy object will be injected
    // to the DefaultsIntentProcessorFactory as singletons.
    override fun create(intent: Intent): IntentProcessor {
        return when (intent) {
            Intent.FetchArticles -> FetchArticlesIntentProcessor()
        }
    }
}