package com.kesicollection.feature.quiz

val initialState = UiState()

data class UiState(
    val isLoading: Boolean = true,
    val showNextButton: Boolean = false,
    val questions: List<UiQuestion> = emptyList(),
    val selectedAnswers: Map<Int, IndexAnswerState> = emptyMap()
)

sealed interface QuizIntent {
    data class FetchQuestionsByName(val name: String) : QuizIntent
    data class SubmitAnswer(
        val questionId: String,
        val selectedIndex: Int
    ) : QuizIntent

    data object HideNextButton : QuizIntent
}

/**
 * Represents the state of an answer in a quiz, indicating whether it's correct or incorrect.
 */
enum class IndexAnswerState {
    Correct, Incorrect
}