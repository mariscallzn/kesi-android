package com.kesicollection.feature.quiztopics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

typealias OnTopicClick = (topicId: String) -> Unit

@Composable
fun QuizTopicsScreen(
    viewModel: QuizTopicsViewModel,
    onTopicClick: OnTopicClick,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    QuizTopicsScreen(
        state = uiState,
        onTopicClick = onTopicClick,
        modifier = modifier,
    )
}

@Composable
fun QuizTopicsScreen(
    state: State,
    onTopicClick: OnTopicClick,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (state.uiState) {
            QuizTopicsUiState.FetchingError -> Text("Error")
            QuizTopicsUiState.Loading -> CircularProgressIndicator()
            is QuizTopicsUiState.Topics -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.uiState.topics, key = { it.id }) {
                        Text(it.name, modifier = Modifier.clickable {
                            onTopicClick(it.id)
                        })
                    }
                }
            }
        }
    }
}