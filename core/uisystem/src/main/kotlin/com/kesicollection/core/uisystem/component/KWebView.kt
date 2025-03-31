package com.kesicollection.core.uisystem.component

import android.text.Html
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun KWebView(
    modifier: Modifier = Modifier
) {
    val surface = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    AndroidView(
        modifier = modifier
            .clip(RoundedCornerShape(5))
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(5))
            .fillMaxSize()
        ,
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                this.webViewClient = WebViewClient()
                this.webChromeClient = WebChromeClient()
                settings.javaScriptEnabled = true
            }
        },
        update = {

            it.loadData(
                """
<!DOCTYPE html>
<html>
<head>
  <title>Background Color Example</title>
  <style>
    body {
      color: rgb(${(onSurface.red * 255).toInt()}, ${(onSurface.green * 255).toInt()}, ${(onSurface.blue * 255).toInt()});
      background-color: rgb(${(surface.red * 255).toInt()}, ${(surface.green * 255).toInt()}, ${(surface.blue * 255).toInt()}); /* Example RGB value */
      margin: 8; /* Optional: Remove default body margin */
    }
  </style>
</head>
<body>
  <h1></h1>
  <p></p>
  <pre>
  <code>
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
  </code>
  </pre>
</body>
</html>
            """.trimIndent(), "text/html", "UTF-8"
            )
        }
    )

}