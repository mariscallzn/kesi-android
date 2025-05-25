package com.kesicollection.feature.discover.intentprocessor

import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.data.usecase.GetDiscoverContentUseCase
import com.kesicollection.feature.discover.Intent
import com.kesicollection.feature.discover.UiState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of [IntentProcessorFactory] for the Discover feature.
 *
 * This factory is responsible for creating the appropriate [IntentProcessor]
 * based on the received [Intent]. It uses dependency injection to obtain
 * necessary use cases.
 *
 * @param getDiscoverContentUseCase The use case for fetching discover content.
 */
@Singleton
class DefaultIntentProcessorFactory @Inject constructor(
    private val getDiscoverContentUseCase: GetDiscoverContentUseCase,
    private val crashlyticsWrapper: CrashlyticsWrapper,
) : IntentProcessorFactory<UiState, Intent> {
    override fun create(intent: Intent): IntentProcessor<UiState> =
        when (intent) {
            Intent.FetchFeatureItems -> FetchFeatureItemsIntentProcessor(
                getDiscoverContentUseCase = getDiscoverContentUseCase,
                crashlyticsWrapper = crashlyticsWrapper,
            )
        }
}