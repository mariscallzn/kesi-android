package com.kesicollection.feature.discover.intentprocessor

import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.Reducer
import com.kesicollection.feature.discover.UiState
import com.kesicollection.feature.discover.contentSample
import kotlinx.coroutines.delay

class FetchFeatureItemsIntentProcessor : IntentProcessor<UiState> {
    override suspend fun processIntent(reducer: (Reducer<UiState>) -> Unit) {
        delay(1500)
        reducer { contentSample }
    }

}