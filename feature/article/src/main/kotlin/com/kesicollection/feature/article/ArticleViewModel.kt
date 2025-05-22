package com.kesicollection.feature.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.core.app.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ArticleViewModel] is responsible for managing the UI state and handling user interactions for the article feature.
 * It acts as the central component for processing user intents and updating the UI state accordingly.
 *
 * This ViewModel adheres to the MVI (Model-View-Intent) architectural pattern, ensuring a unidirectional flow of data.
 *
 * @property intentProcessorFactory Factory used to create [IntentProcessor] instances based on incoming intents.
 * This allows for different intents to be processed by different [IntentProcessor].
 * @property dispatcher The [CoroutineDispatcher] used for performing background tasks and asynchronous operations,
 * ensuring that UI updates are handled efficiently.
 */
@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val intentProcessorFactory: IntentProcessorFactory<UiArticleState, Intent>,
) : ViewModel() {

    private var _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val intentFlow = MutableSharedFlow<Intent>()

    init {
        viewModelScope.launch(dispatcher) {
            intentFlow.collect {
                processIntent(it)
            }
        }
    }

    fun sendIntent(intent: Intent) {
        viewModelScope.launch(dispatcher) {
            intentFlow.emit(intent)
        }
    }

    private fun reducer(reducer: Reducer<UiArticleState>) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    private suspend fun processIntent(intent: Intent) {
        intentProcessorFactory.create(intent).processIntent(::reducer)
    }
}