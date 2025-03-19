package com.kesicollection.feature.quiztopics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

typealias OnTopicClick = (id: String, name: String) -> Unit

@Composable
fun QuizTopicsScreen(
    viewModel: QuizTopicsViewModel,
    onTopicClick: OnTopicClick,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendEvent(QuizTopicsUiEvent.FetchTopics)
    }

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
    Scaffold(modifier = modifier) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (state.uiState) {
                QuizTopicsUiState.FetchingError -> Text("Error")
                QuizTopicsUiState.Loading -> CircularProgressIndicator()
                is QuizTopicsUiState.FetchedTopics -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.uiState.topics, key = { it.id }) {
                            Button(
                                { onTopicClick(it.id, it.name) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    it.name, modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}