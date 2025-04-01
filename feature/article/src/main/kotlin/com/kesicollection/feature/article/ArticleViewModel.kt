package com.kesicollection.feature.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.feature.article.intent.IntentProcessorFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val intentProcessorFactory: IntentProcessorFactory,
) : ViewModel() {

    private var _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val intentFlow = MutableSharedFlow<Intent>()

    init {
        viewModelScope.launch {
            intentFlow.collect {
                processIntent(it)
            }
        }
    }

    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }

    private fun reducer(reducer: Reducer) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    private suspend fun processIntent(intent: Intent) {
        intentProcessorFactory.create(intent).processIntent(::reducer)
    }
}