package com.kesicollection.feature.quiz

data class QuizUiState(
    val questions: List<UiQuestion> = emptyList()
)

val initialState = QuizUiState().copy(questions = emptyList())
