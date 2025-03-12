package com.kesicollection.feature.quiz

//TODO use it to define global properties and specific states that the ui should show.
//sealed interface QuizUiState {
//    val errorState: ErrorState<SomeType>? = null,
//    val loadingStates: Set<LoadingState> = setOf(LoadingState.GettingSummary), or simple isLoading: Boolean.
//}
data class QuizUiState(
    val questions: List<String> = emptyList()
)
