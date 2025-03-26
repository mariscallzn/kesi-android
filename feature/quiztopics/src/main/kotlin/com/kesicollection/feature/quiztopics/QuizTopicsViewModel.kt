package com.kesicollection.feature.quiztopics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This ViewModel implementations follows MVI. For more info read:
 * - [proandroiddev - MVI](https://proandroiddev.com/mvi-architecture-with-kotlin-flows-and-channels-d36820b2028d)
 * - [Orbit-MVI](https://github.com/orbit-mvi/orbit-mvi)
 */
@HiltViewModel
class QuizTopicsViewModel @Inject constructor(
    private val repository: QuizTopicsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        State(uiState = QuizTopicsUiState.Loading)
    )
    val uiState: StateFlow<State>
        get() = _uiState.asStateFlow()

    private val intent = MutableSharedFlow<QuizTopicsIntent>()

    init {
        viewModelScope.launch {
            intent.asSharedFlow().collect {
                when (it) {
                    QuizTopicsIntent.FetchTopics -> fetchTopics()
                }
            }
        }
    }

    fun intent(userIntent: QuizTopicsIntent) {
        viewModelScope.launch { intent.emit(userIntent) }
    }

    private fun reduce(reducer: State.() -> State) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    private fun fetchTopics() {
        viewModelScope.launch {
            try {
                val topics = repository.fetchTopics().getOrThrow()
                reduce { copy(uiState = QuizTopicsUiState.FetchedTopics(topics)) }
            } catch (e: Exception) {
                reduce { copy(uiState = QuizTopicsUiState.FetchingError) }
            }
        }
    }
}