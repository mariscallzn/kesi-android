package com.kesicollection.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.articles.di.ArticlesDefaultDispatcher
import com.kesicollection.articles.intentprocessor.IntentProcessorFactory
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
 * This class manages the UI state of the Articles screen and handles incoming user intents.
 * It uses a unidirectional data flow pattern, where UI events are translated into [Intent]s,
 * which are then processed to update the [uiState].
 *
 * @property intentProcessorFactory A factory responsible for creating [IntentProcessor] instances
 *                                  based on the received [Intent].
 * @property dispatcher The [CoroutineDispatcher] used for launching coroutines within the ViewModel.
 *                     This allows for background processing and non-blocking UI updates.
 */
@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val intentProcessorFactory: IntentProcessorFactory,
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

    private fun reducer(reducer: Reducer) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    private fun processIntent(intent: Intent) {
        viewModelScope.launch(dispatcher) {
            intentProcessorFactory.create(intent).processIntent(reducer = ::reducer)
        }
    }
}