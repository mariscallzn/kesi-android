package com.kesicollection.feature.quiztopics

import com.kesicollection.core.model.Topic

data class State(
    val uiState: QuizTopicsUiState
)

/**
 * In this case we will use a sealed class to illustrate it's implementation,
 * but you can always use a single data class with properties to represent the state.
 *
 * By using `sealed interface` you enforce state type and structure (But it doesn't mean that you should
 * always enforce it)
 */
sealed interface QuizTopicsUiState {
    data object Loading : QuizTopicsUiState
    data object FetchingError : QuizTopicsUiState
    data class Topics(
        val topics: List<Topic>
    ) : QuizTopicsUiState
}