package com.kesicollection.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.app.IntentProcessorFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val intentProcessorFactory: IntentProcessorFactory<UiState, Intent>,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val processIntent = MutableSharedFlow<Intent>()

    init {
        viewModelScope.launch(dispatcher) {
            processIntent.collect {
                processIntent(it)
            }
        }
    }

    fun sendIntent(intent: Intent) {
        viewModelScope.launch(dispatcher) {
            processIntent.emit(intent)
        }
    }

    private fun reducer(reduce: UiState.() -> UiState) {
        val newState = _uiState.value.reduce()
        _uiState.value = newState
    }

    private suspend fun processIntent(intent: Intent) {
        intentProcessorFactory.create(intent).processIntent(::reducer)
    }
}