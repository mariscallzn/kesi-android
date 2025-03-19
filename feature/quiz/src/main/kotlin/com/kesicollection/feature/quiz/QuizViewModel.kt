package com.kesicollection.feature.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TODO: Re-define the MVX pattern here
 */
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository,
) : ViewModel() {
    val uiState: StateFlow<QuizUiState>
        get() = _uiState.asStateFlow().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialState
        )
    private val _uiState = MutableStateFlow(initialState)

    fun fetchQuestionsByTopicName(name: String) {
        viewModelScope.launch {
            val questions = repository.fetchQuestionByTopicName(name).getOrThrow()
            _uiState.value = _uiState.value.copy(questions = questions)
        }
    }

}