package com.kesicollection.feature.article.intent

import com.kesicollection.feature.article.Reducer

interface IntentProcessor {
    suspend fun processIntent(reducer: (Reducer) -> Unit)
}