package com.kesicollection.feature.quiz

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.uisystem.theme.KesiTheme

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    QuizScreen(
        uiState = uiState,
        modifier = modifier
    )
}


@Composable
internal fun QuizScreen(
    uiState: QuizUiState,
    modifier: Modifier = Modifier
) {
    Text("Placeholder ${uiState.questions.size}", modifier = modifier)
}

@Preview
@Composable
private fun PreviewQuizScreen() {
    KesiTheme {
        QuizScreen(uiState = QuizUiState().copy(questions = List(5) { "mock" }))
    }
}