package com.kesicollection.feature.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository,
    private val uiQuestionTypeFactory: UIQuestionTypeFactory
) : ViewModel() {
    val uiState: StateFlow<UiState>
        get() = _uiState

    private val _uiState = MutableStateFlow(initialState)

    private val quizIntent = MutableSharedFlow<QuizIntent>()

    init {
        viewModelScope.launch {
            quizIntent.collect {
                when (it) {
                    is QuizIntent.FetchQuestionsByName -> fetchQuestionsByTopicName(it.name)
                    is QuizIntent.SubmitAnswer -> selectAnswer(it.questionId, it.selectedIndex)
                    QuizIntent.HideNextButton -> reducer { copy(showNextButton = false) }
                    is QuizIntent.QuestionSelected -> questionSelected(it.questionIndex)
                }
            }
        }
    }

    private fun questionSelected(questionIndex: Int) {
        reducer { copy(showNextButton = uiState.value.selectedAnswers[questionIndex] != null) }
    }

    private fun selectAnswer(questionId: String, selectedIndex: Int) {
        viewModelScope.launch {
            //draft in db or some
            reducer {
                val questionIndex = questions.indexOfFirst { it.metadata.id == questionId }
                val status =
                    if (questions[questionIndex].metadata.correctAnswerIndex == selectedIndex)
                        IndexAnswerState.Correct else IndexAnswerState.Incorrect

                val updatedMap = selectedAnswers.toMutableMap().apply {
                    put(questionIndex, status)
                }
                copy(
                    selectedAnswers = updatedMap,
                    showNextButton = true
                )
            }
        }
    }

    private fun fetchQuestionsByTopicName(name: String) {
        viewModelScope.launch {
            val questions = repository.fetchQuestionByTopicName(name).getOrThrow().map {
                UiQuestion(
                    metadata = it,
                    question = uiQuestionTypeFactory.create(it)
                )
            }
            reducer { copy(questions = questions, isLoading = false) }
        }
    }

    private fun reducer(reduce: UiState.() -> UiState) {
        val newState = _uiState.value.reduce()
        _uiState.value = newState
    }

    fun sendIntent(intent: QuizIntent) {
        viewModelScope.launch { quizIntent.emit(intent) }
    }

}