package com.kesicollection.feature.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.model.Question
import com.kesicollection.core.model.Topic
import com.kesicollection.core.uisystem.component.ExpandableProgressCard
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiz.component.OnSelectedAnswer
import com.kesicollection.feature.quiz.component.QuestionCard
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    topic: Topic,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchQuestionsByTopicName(topic.name)
    }

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
    topic: Topic,
    onSelectedAnswer: OnSelectedAnswer,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { uiState.questions.size }

    KScaffold(
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
                    Text(text = topic.name, style = MaterialTheme.typography.headlineSmall)
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
        val scrollableState = rememberScrollState()
        var isProgressCardExpanded by rememberSaveable { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollableState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var progress by rememberSaveable {
                mutableFloatStateOf(0f)
            }

            LaunchedEffect(pagerState.currentPageOffsetFraction) {
                snapshotFlow { pagerState.currentPageOffsetFraction }
                    .collect { offset ->
                        progress =
                            (pagerState.currentPage + offset) / (uiState.questions.size - 1).toFloat()
                    }
            }

            ExpandableProgressCard(
                progress = { progress },
                onExpandedClick = { isProgressCardExpanded = !isProgressCardExpanded },
                state = { isProgressCardExpanded },
                currentIndex = { Text("${pagerState.settledPage + 1}") },
                total = { Text("${uiState.questions.size}") },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(165.dp)
                )
            }
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
                contentPadding = PaddingValues(16.dp),
                pageSpacing = 16.dp,
            ) { page ->
                val question = uiState.questions[page]
                val coroutineScope = rememberCoroutineScope()
                Column {
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
            topic = Topic(
                id = "",
                name = "Jetpack Compose"
            ),
            onSelectedAnswer = { _, _ -> },
            onNavigateUp = {},
            uiState = QuizUiState().copy(
                questions = listOf(
                    Question(
                        question = "Consider a complex custom layout in Compose that requires precise measurement and placement of children based on dynamic content. How would you optimize its performance to minimize recompositions and layout passes, especially when dealing with large datasets?",
                        options = listOf(
                            "Using `Modifier.layout` with direct manipulation of `Placeable` instances and extensive caching.",
                            "Relying solely on built-in Compose layouts and modifiers, hoping for automatic optimization.",
                            "Creating a custom `Layout` composable with minimal logic and relying on recomposition for updates.",
                            "Using `SubcomposeLayout` for all child elements, regardless of complexity."
                        ),
                        topic = "Jetpack Compose",
                        correctAnswerIndex = 0,
                        difficulty = "Hard",
                        explanation = "Direct manipulation of `Placeable` instances in `Modifier.layout` and strategic caching allows for fine-grained control and performance optimization. It minimizes unnecessary recompositions and layout calculations.",
                        tags = listOf("performance", "custom layout", "optimization")
                    ),
                )
            )
        )
    }
}