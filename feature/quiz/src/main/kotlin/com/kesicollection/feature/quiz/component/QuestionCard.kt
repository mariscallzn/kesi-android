package com.kesicollection.feature.quiz.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.model.Difficulty
import com.kesicollection.core.model.Question
import com.kesicollection.core.model.QuestionType
import com.kesicollection.core.model.Topic
import com.kesicollection.core.uisystem.component.AnsweredOption
import com.kesicollection.core.uisystem.component.SingleOption
import com.kesicollection.core.uisystem.component.SingleOptionDefaults.colorAnimation
import com.kesicollection.core.uisystem.component.SingleOptionDefaults.trailingScaleAnimation
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiz.R
import com.kesicollection.feature.quiz.UiQuestion

/**
 * A composable function that displays a question card with multiple-choice options.
 * The type of question displayed (e.g., text, image, code) is determined by the [UiQuestion] passed.
 *
 * @sample com.kesicollection.feature.quiz.component.ExampleQuestionCard
 *
 * @param question The [UiQuestion] data to display in the card.
 * It contains the question content and metadata, and defines what type of question will be displayed.
 * @param onSelectedAnswer A callback function triggered when an answer is selected.
 *  It provides the question ID and the index of the selected answer.
 * @param modifier Modifiers for styling the card.
 * @param state The state of the question card, which tracks the selected answer.
 *  Defaults to a new [QuestionCardState] if not provided.
 */
@Composable
fun QuestionCard(
    question: UiQuestion,
    onSelectedAnswer: (questionId: String, selectedIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
    state: QuestionCardState = rememberQuestionCardState(),
) {
    Card(modifier = modifier.animateContentSize(), shape = MaterialTheme.shapes.large) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .animateContentSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DisplayQuestion(question.question)
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.feature_quiz_choose_answer),
                style = MaterialTheme.typography.labelLarge
            )
            question.metadata.options.forEachIndexed { index, item ->
                key(item) {
                    val color by colorAnimation(
                        state.getAnswerOption(
                            index,
                            question.metadata.correctAnswerIndex
                        )
                    )

                    SingleOption(
                        trailing = {
                            val scale by trailingScaleAnimation(state.isAnswerSelected(index))
                            Icon(
                                imageVector = if (
                                    index == question.metadata.correctAnswerIndex
                                ) KIcon.Check else KIcon.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .widthIn(24.dp)
                                    .graphicsLayer {
                                        val scale = scale.coerceIn(0f, 1f)
                                        scaleX = scale
                                        scaleY = scale
                                        transformOrigin = TransformOrigin.Center
                                    }
                                    .drawWithContent {
                                        drawCircle(color = color)
                                        drawContent()
                                    }
                            )
                        },
                        color = { color },
                        onClick = {
                            state.selectAnswer(index)
                            onSelectedAnswer(question.metadata.id, index)
                        },
                    ) {
                        Text(item, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (index == question.metadata.options.lastIndex) {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            question.metadata.explanation?.let {
                if (state.selectedAnswerState.value >= 0) {
                    Text(it, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

/**
 * Interface for different types of UI questions.
 *
 * This interface defines a contract for displaying a question in the user interface.
 * Each implementation of this interface represents a different type of question
 * (e.g., text-based, image-based, code snippet-based).
 *
 * The [Question] composable function is responsible for rendering the specific question
 * content.
 */
interface UIQuestionType {
    @Composable
    fun Question(modifier: Modifier = Modifier)
}

/**
 * Represents a question that is displayed as plain text.
 *
 * @property question The text content of the question.
 */
class TextQuestion(private val question: String) : UIQuestionType {
    @Composable
    override fun Question(modifier: Modifier) {
        Text(text = question, style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * Represents a question that requires an image to be displayed.
 *
 * This class implements the [UIQuestionType] interface and is responsible for rendering a question
 * that includes an image. The `question` parameter provides the text description of the question
 * related to the image.
 *
 * The [Question] composable function is a placeholder and currently displays a simple text
 * representation of the question. It's intended to be replaced with an actual image display
 * mechanism in the future.
 *
 * @property path The path associated with the image direction.
 */
class ImageQuestion(private val path: String) : UIQuestionType {
    @Composable
    override fun Question(modifier: Modifier) {
        //TODO: This is a Placeholder for image question
        Text(text = "Image question: $path")
        // Image(painter = ..., contentDescription = null, modifier = modifier)
    }
}

/**
 * Represents a question that displays a code snippet.
 *
 * @property code The code snippet to be displayed as part of the question.
 */
class CodeSnippetQuestion(private val code: String) : UIQuestionType {
    @Composable
    override fun Question(modifier: Modifier) {
        //TODO: This is a Placeholder for code snippet question
        Text(text = "Code Snippet question: $code")
        // Text(text = question, modifier = modifier)
    }
}

/**
 * Displays the question based on the provided [questionType].
 *
 * This composable function takes a [UIQuestionType] and a [Modifier] as input.
 * It then delegates the rendering of the question to the `Question` function
 * defined within the provided [questionType]. This allows for displaying different
 * types of questions (e.g., text, image, code snippet) using a unified interface.
 *
 * @param questionType The type of question to display, which implements the [UIQuestionType] interface.
 * @param modifier The [Modifier] to be applied to the question composable.
 */
@Composable
fun DisplayQuestion(
    questionType: UIQuestionType,
    modifier: Modifier = Modifier,
) {
    questionType.Question(modifier)
}

/**
 * Creates and remembers a [QuestionCardState] instance.
 *
 * This function provides a state holder for the [QuestionCard] composable, allowing it to manage
 * the selected answer index and the state of answer options. It uses [rememberSaveable] to ensure
 * that the state is preserved across configuration changes and process death.
 *
 * @param selectedIndex The initial selected answer index. Defaults to -1, indicating no selection.
 * @return A [QuestionCardState] instance that is remembered across recompositions.
 */
@Composable
fun rememberQuestionCardState(selectedIndex: Int = -1) = rememberSaveable(
    saver = QuestionCardState.Saver
) {
    QuestionCardState(selectedIndex)
}

/**
 * Represents the state of a question card, including the currently selected answer.
 *
 * @property selectedIndex The initial index of the selected answer. Defaults to -1, indicating no selection.
 */
class QuestionCardState(
    selectedIndex: Int = -1
) {
    var selectedAnswerState = mutableIntStateOf(selectedIndex)
        private set

    fun selectAnswer(index: Int) {
        selectedAnswerState.value = if (selectedAnswerState.value == index) -1 else index
    }

    fun getAnswerOption(index: Int, answerIndex: Int): AnsweredOption =
        when {
            selectedAnswerState.value == index && selectedAnswerState.value == answerIndex -> AnsweredOption.CORRECT
            selectedAnswerState.value == index && selectedAnswerState.value >= 0 && selectedAnswerState.value != answerIndex -> AnsweredOption.INCORRECT
            else -> AnsweredOption.DEFAULT
        }

    fun isAnswerSelected(index: Int): Boolean =
        selectedAnswerState.value == index && selectedAnswerState.value >= 0

    companion object {
        val Saver: Saver<QuestionCardState, *> = listSaver(
            save = { listOf(it.selectedAnswerState.value) },
            restore = {
                QuestionCardState(it[0])
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewLightDarkQuestionCard_NoneSelected() {
    ExampleQuestionCard(modifier = Modifier.padding(8.dp))
}

@PreviewLightDark
@Composable
private fun PreviewLightDarkQuestionCard_Correct() {
    ExampleQuestionCard(modifier = Modifier.padding(8.dp), selectedIndex = 1)
}

@PreviewLightDark
@Composable
private fun PreviewLightDarkQuestionCard_Incorrect() {
    ExampleQuestionCard(modifier = Modifier.padding(8.dp), selectedIndex = 0)
}

@Composable
private fun ExampleQuestionCard(
    modifier: Modifier = Modifier,
    selectedIndex: Int = -1
) {
    KesiTheme {
        KesiTheme {
            QuestionCard(
                state = rememberQuestionCardState(selectedIndex),
                question = UiQuestion(
                    Question(
                        id = "1",
                        content = "What is the capital of France?",
                        options = listOf("London", "Paris", "Berlin", "Madrid"),
                        topic = Topic(name = "Geography"),
                        correctAnswerIndex = 1,
                        questionType = QuestionType.Text,
                        difficulty = Difficulty.Easy,
                        explanation = "Paris is the capital of France.",
                    ), TextQuestion("What is the capital of France?")
                ),
                onSelectedAnswer = { _, _ -> },
                modifier = modifier,
            )
        }
    }
}