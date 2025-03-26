package com.kesicollection.feature.quiztopics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.model.Topic
import com.kesicollection.core.model.TopicCard
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiztopics.component.TopicCard

typealias OnTopicClick = (id: String, name: String) -> Unit

@Composable
fun QuizTopicsScreen(
    viewModel: QuizTopicsViewModel,
    onTopicClick: OnTopicClick,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.intent(QuizTopicsIntent.FetchTopics)
    }

    QuizTopicsScreen(
        state = uiState.uiState,
        onTopicClick = onTopicClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizTopicsScreen(
    state: QuizTopicsUiState,
    onTopicClick: OnTopicClick,
    modifier: Modifier = Modifier
) {
    KScaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = {
                Text(
                    stringResource(R.string.feature_quiztopics_top_bar_title),
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when (state) {
                QuizTopicsUiState.FetchingError -> Text(
                    "Error",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )

                QuizTopicsUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )

                is QuizTopicsUiState.FetchedTopics -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        items(state.topicCards, key = { it.topic.id }) { topicCard ->
                            TopicCard(
                                onCardClick = { onTopicClick(it.topic.id, it.topic.name) },
                                topicCard = topicCard,
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .animateItem()
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewQuizTopics() {
    KesiTheme {
        QuizTopicsScreen(
            state = State(uiState = QuizTopicsUiState.FetchedTopics(topicCards = List(20) {
                TopicCard(
                    Topic(
                        id = it.toString(),
                        name = "Topic $it"
                    ),
                    it * 6
                )
            })).uiState,
            onTopicClick = { _, _ -> },
        )
    }
}