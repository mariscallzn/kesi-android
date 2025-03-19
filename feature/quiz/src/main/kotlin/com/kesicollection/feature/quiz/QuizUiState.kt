package com.kesicollection.feature.quiz

import com.kesicollection.core.model.Question

data class QuizUiState(
    val questions: List<Question> = emptyList()
)

val initialState = QuizUiState().copy(questions = emptyList())
