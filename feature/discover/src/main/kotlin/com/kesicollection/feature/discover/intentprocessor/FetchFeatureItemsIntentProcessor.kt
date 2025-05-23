package com.kesicollection.feature.discover.intentprocessor

import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.Reducer
import com.kesicollection.core.model.ErrorState
import com.kesicollection.data.usecase.GetDiscoverContentUseCase
import com.kesicollection.feature.discover.DiscoverErrors
import com.kesicollection.feature.discover.UiState
import com.kesicollection.feature.discover.asDiscoverContent
import com.kesicollection.feature.discover.contentSample
import kotlinx.coroutines.delay

/**
 * Processes the intent to fetch feature items for the Discover screen.
 *
 * This processor utilizes the [GetDiscoverContentUseCase] to retrieve discover content.
 * On success, it maps the result to [UiState.DiscoverContent] and updates the UI through the provided [Reducer].
 * On failure, it maps the exception to [UiState.Error] with a [DiscoverErrors.GenericError]
 * and updates the UI.
 *
 * @property getDiscoverContentUseCase The use case responsible for fetching discover content.
 */
class FetchFeatureItemsIntentProcessor(
    val getDiscoverContentUseCase: GetDiscoverContentUseCase
) : IntentProcessor<UiState> {
    override suspend fun processIntent(reducer: (Reducer<UiState>) -> Unit) {
        try {
            val result = getDiscoverContentUseCase().getOrThrow().asDiscoverContent()
            reducer { result }
        } catch (e: Exception) {
            reducer { UiState.Error(ErrorState(DiscoverErrors.GenericError, e.message)) }
        }
    }

}