package com.kesicollection.feature.quiz

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.uisystem.component.ExpandableProgressCard
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiz.component.OnSelectedAnswer
import com.kesicollection.feature.quiz.component.QuestionCard
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    topic: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    QuizScreen(
        uiState = uiState,
        topic = topic,
        onSelectedAnswer = { _, _ -> } /*viewModel::some*/,
        onNavigateUp = onNavigateUp,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuizScreen(
    uiState: QuizUiState,
    topic: String,
    onSelectedAnswer: OnSelectedAnswer,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { uiState.questions.size }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = KIcon.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                title = {
                    Text(text = topic, style = MaterialTheme.typography.headlineSmall)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { }
            ) {
                Icon(KIcon.ArrowForward, stringResource(R.string.feature_quiz_continue))
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var isProgressCardExpanded by rememberSaveable { mutableStateOf(false) }
            val progress by rememberSaveable(pagerState.settledPage) {
                mutableFloatStateOf(
                    (pagerState.settledPage + 1).toFloat() / uiState.questions.size.toFloat()
                )
            }
            val animatedProgress by animateFloatAsState(progress)
            ExpandableProgressCard(
                progress = { animatedProgress },
                onExpandedClick = { isProgressCardExpanded = !isProgressCardExpanded },
                state = { isProgressCardExpanded },
                currentIndex = { Text("${pagerState.settledPage + 1}") },
                total = { Text("${uiState.questions.size}") },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                //TODO: Create progress content
            }
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                pageSpacing = 16.dp,
            ) { page ->
                val question = uiState.questions[page]
                val coroutineScope = rememberCoroutineScope()
                val scrollableState = rememberScrollState()
                Column(modifier = Modifier.verticalScroll(scrollableState)) {
                    QuestionCard(
                        question = question,
                        onSelectedAnswer = { q, i ->
                            coroutineScope.launch {
                                scrollableState.animateScrollTo(Int.MAX_VALUE)
                                onSelectedAnswer(q, i)
                            }
                        }
                    )
                    Spacer(Modifier.height(88.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewQuizScreen() {
    Example(modifier = Modifier.fillMaxSize())
}

@PreviewLightDark
@Composable
private fun PreviewLightDarkQuizScreen() {
    Example()
}

@Composable
private fun Example(modifier: Modifier = Modifier) {
    KesiTheme {
        QuizScreen(
            modifier = modifier,
            topic = "Jetpack Compose",
            onSelectedAnswer = { _, _ -> },
            onNavigateUp = {},
            uiState = QuizUiState().copy(questions = generateQuestions())
        )
    }
}