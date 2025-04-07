package com.kesicollection.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.articles.di.ArticlesDefaultDispatcher
import com.kesicollection.core.uisystem.IntentProcessor
import com.kesicollection.core.uisystem.IntentProcessorFactory
import com.kesicollection.core.uisystem.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the Articles screen.
 *
 * This ViewModel is responsible for managing the UI state ([uiState]) of the Articles screen and
 * processing user interactions. It follows a unidirectional data flow (UDF) pattern.
 *
 * User interactions are represented as [Intent]s. These are sent to the ViewModel and processed.
 * Processing an [Intent] can result in a state change, which is reflected in the [uiState].
 *
 * The ViewModel utilizes an [IntentProcessorFactory] to create [IntentProcessor] instances that handle
 * the logic for each specific [Intent] type.
 *
 * @property intentProcessorFactory A factory for creating [IntentProcessor] instances based on the
 *                                  received [Intent]. Each intent type has its own associated
 *                                  [IntentProcessor] for specialized handling.
 * @property dispatcher The [CoroutineDispatcher] used for all coroutine operations within this
 *                     ViewModel, ensuring that background tasks and state updates are managed
 *                     efficiently without blocking the main thread.
 */
@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val intentProcessorFactory: IntentProcessorFactory<UiArticlesState, Intent>,
    @ArticlesDefaultDispatcher
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
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

    private fun reducer(reducer: Reducer<UiArticlesState>) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    private fun processIntent(intent: Intent) {
        viewModelScope.launch(dispatcher) {
            intentProcessorFactory.create(intent).processIntent(reducer = ::reducer)
        }
    }
}