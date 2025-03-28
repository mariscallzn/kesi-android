package com.kesicollection.feature.quiz

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.model.Difficulty
import com.kesicollection.core.model.Question
import com.kesicollection.core.model.QuestionType
import com.kesicollection.core.model.Topic
import com.kesicollection.core.uisystem.component.ExpandableProgressCard
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiz.component.ProgressTable
import com.kesicollection.feature.quiz.component.QuestionCard
import com.kesicollection.feature.quiz.component.TextQuestion
import com.kesicollection.feature.quiz.component.rememberProgressTableState
import kotlinx.coroutines.launch

/**
 * Composable function that displays the quiz screen.
 *
 * This function acts as an entry point for the quiz feature, handling the overall
 * quiz flow and navigation. It fetches questions from the [QuizViewModel]
 * based on the provided [topic] and updates the UI accordingly.
 *
 * @param viewModel The [QuizViewModel] instance to use for data fetching and managing
 *                  the quiz state.
 * @param topic The [Topic] for which the quiz is being taken.
 * @param onNavigateUp Callback to be invoked when the user wants to navigate up
 *                     from the quiz screen.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    topic: Topic,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(QuizIntent.FetchQuestionsByName(topic.name))
    }

    QuizScreen(
        uiState = uiState,
        topic = topic,
        onSelectedAnswer = viewModel::sendIntent,
        hideNextButton = viewModel::sendIntent,
        onNavigateUp = onNavigateUp,
        modifier = modifier
    )
}


/**
 * Composable function that displays the quiz screen.
 *
 * This screen presents a series of questions to the user, allowing them to select answers and track their progress.
 * It features a top app bar, a floating action button for navigation, and a progress card to visualize the user's advancement.
 * The screen dynamically updates based on the [uiState] and allows interaction through callbacks for answer submission and navigation.
 *
 * @param uiState The current state of the quiz UI, containing questions, loading status, and other relevant information.
 * @param topic The topic associated with the quiz, used to display the quiz title.
 * @param onSelectedAnswer Callback invoked when the user selects an answer for a question. It takes a [QuizIntent.SubmitAnswer] as parameter.
 * @param hideNextButton Callback to be invoked to hide next button. It takes a [QuizIntent.HideNextButton] as parameter.
 * @param onNavigateUp Callback invoked when the user wants to navigate back from the quiz screen.
 * @param modifier Modifier to be applied to the root layout of the quiz screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuizScreen(
    uiState: UiState,
    topic: Topic,
    onSelectedAnswer: (QuizIntent.SubmitAnswer) -> Unit,
    hideNextButton: (QuizIntent.HideNextButton) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { uiState.questions.size }
    val coroutineScope = rememberCoroutineScope()
    val fabVisibility by animateFloatAsState(if (uiState.showNextButton) 1f else 0f)
    val progressTableState = rememberProgressTableState(
        { uiState.questions.size }
    ) { pagerState.currentPage }
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
                modifier = Modifier
                    .graphicsLayer {
                        scaleY = fabVisibility
                        scaleX = fabVisibility
                        transformOrigin = TransformOrigin.Center
                    },
                onClick = {
                    if (pagerState.currentPage < uiState.questions.size) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                    hideNextButton(QuizIntent.HideNextButton)
                }
            ) {
                Icon(KIcon.ArrowForward, stringResource(R.string.feature_quiz_continue))
            }
        },
        modifier = modifier
    ) { innerPadding ->
        val scrollableState = rememberScrollState()
        when {
            uiState.isLoading && uiState.questions.isEmpty() -> CircularProgressIndicator(
                Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )

            !uiState.isLoading && uiState.questions.isNotEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(scrollableState),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var isProgressCardExpanded by rememberSaveable { mutableStateOf(true) }
                    var progress by rememberSaveable {
                        mutableFloatStateOf(0f)
                    }

                    LaunchedEffect(pagerState.currentPageOffsetFraction) {
                        snapshotFlow { pagerState.currentPageOffsetFraction }
                            .collect { offset ->
                                val totalQuestions = (uiState.questions.size - 1).toFloat()
                                progress = if (totalQuestions > 0) {
                                    (pagerState.currentPage + offset) / totalQuestions
                                } else {
                                    0f
                                }
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
                        Surface(
                            color = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                        ) {
                            ProgressTable(
                                state = progressTableState,
                                onIndexClick = { },
                                selectedAnswers = { uiState.selectedAnswers }
                            )
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        verticalAlignment = Alignment.Top,
                        contentPadding = PaddingValues(16.dp),
                        pageSpacing = 16.dp,
                        userScrollEnabled = false
                    ) { page ->
                        val question = uiState.questions[page]
                        Column {
                            QuestionCard(
                                question = question,
                                onSelectedAnswer = { q, i ->
                                    coroutineScope.launch {
                                        scrollableState.animateScrollTo(Int.MAX_VALUE)
                                        onSelectedAnswer(
                                            QuizIntent.SubmitAnswer(q, i)
                                        )
                                    }
                                }
                            )
                            Spacer(Modifier.height(88.dp))
                        }
                    }
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
            onSelectedAnswer = { },
            hideNextButton = { },
            onNavigateUp = {},
            uiState = UiState(
                isLoading = false,
                questions = listOf(
                    UiQuestion(
                        Question(
                            content = "Consider a complex custom layout in Compose that requires precise measurement and placement of children based on dynamic content. How would you optimize its performance to minimize recompositions and layout passes, especially when dealing with large datasets?",
                            options = listOf(
                                "Using `Modifier.layout` with direct manipulation of `Placeable` instances and extensive caching.",
                                "Relying solely on built-in Compose layouts and modifiers, hoping for automatic optimization.",
                                "Creating a custom `Layout` composable with minimal logic and relying on recomposition for updates.",
                                "Using `SubcomposeLayout` for all child elements, regardless of complexity."
                            ),
                            topic = Topic(name = "Jetpack Compose"),
                            correctAnswerIndex = 0,
                            difficulty = Difficulty.Hard,
                            explanation = "Direct manipulation of `Placeable` instances in `Modifier.layout` and strategic caching allows for fine-grained control and performance optimization. It minimizes unnecessary recompositions and layout calculations.",
                            tags = emptyList(),
                            questionType = QuestionType.Text
                        ),
                        TextQuestion("Consider a complex custom layout in Compose that requires precise measurement and placement of children based on dynamic content. How would you optimize its performance to minimize recompositions and layout passes, especially when dealing with large datasets?")
                    )
                )
            )
        )
    }
}