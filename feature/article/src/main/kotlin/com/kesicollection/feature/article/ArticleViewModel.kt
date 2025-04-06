package com.kesicollection.feature.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.feature.article.di.ArticleDefaultDispatcher
import com.kesicollection.feature.article.intent.IntentProcessor
import com.kesicollection.feature.article.intent.IntentProcessorFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ArticleViewModel] is the ViewModel for the article feature.
 * It handles the UI state and processes user intents to update the state accordingly.
 *
 * This ViewModel follows the MVI (Model-View-Intent) pattern.
 *
 * @property intentProcessorFactory Factory for creating [IntentProcessor] instances.
 * @property dispatcher The CoroutineDispatcher for background operations.
 */
@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val intentProcessorFactory: IntentProcessorFactory,
    @ArticleDefaultDispatcher
    private val dispatcher: CoroutineDispatcher,
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

    private fun reducer(reducer: Reducer) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    private suspend fun processIntent(intent: Intent) {
        intentProcessorFactory.create(intent).processIntent(::reducer)
    }
}