package com.kesicollection.feature.article.intent

import com.kesicollection.feature.article.Reducer
import kotlinx.coroutines.delay

class FetchArticleIntentProcessor(
    private val articleId: String,
) : IntentProcessor {
    override suspend fun processIntent(reducer: (Reducer) -> Unit) {
        reducer { copy(isLoading = true) }
        delay(1500) //Fetch from network
        reducer { copy(isLoading = false, tmpText = "This is the loaded content $articleId") }
    }
}