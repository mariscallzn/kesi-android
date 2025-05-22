package com.kesicollection.feature.discover.intentprocessor

import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.feature.discover.Intent
import com.kesicollection.feature.discover.UiState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultIntentProcessorFactory @Inject constructor(

) : IntentProcessorFactory<UiState, Intent> {
    override fun create(intent: Intent): IntentProcessor<UiState> =
        when (intent) {
            Intent.FetchFeatureItems -> FetchFeatureItemsIntentProcessor()
        }
}