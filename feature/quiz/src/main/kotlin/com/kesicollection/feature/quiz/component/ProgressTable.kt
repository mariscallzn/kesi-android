package com.kesicollection.feature.quiz.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiz.IndexAnswerState

/**
 * A composable function that displays a table of progress indicators for a quiz or assessment.
 *
 * Each indicator represents a question or index in the quiz. The table shows the status of each question:
 * - Correct: The question was answered correctly.
 * - Incorrect: The question was answered incorrectly.
 * - Unanswered: The question has not been answered yet (indicated by reduced alpha).
 * - Current: The question currently being answered (indicated by a different border color).
 *
 * @sample com.kesicollection.feature.quiz.component.ProgressTablePreview
 *
 * @param onIndexClick A callback that is invoked when an index item is clicked. It provides the index of the clicked item.
 * @param selectedAnswers A lambda function that provides a map of selected answers. The key is the index, and the value is the [IndexAnswerState] for that index.
 * @param modifier Modifier for styling the layout.
 * @param state The [ProgressTableState] that holds the state of the progress table. Use [rememberProgressTableState] to create and remember the state.
 * @param colors The [ProgressTableColors] that define the colors used for different states. Use [ProgressTableDefaults.progressTableColors] to get default colors.
 */
@Composable
fun ProgressTable(
    onIndexClick: (Int) -> Unit,
    selectedAnswers: () -> Map<Int, IndexAnswerState>,
    modifier: Modifier = Modifier,
    state: ProgressTableState = rememberProgressTableState({ 1 }) { 0 },
    colors: ProgressTableColors = ProgressTableDefaults.progressTableColors()
) {
    FlowRow(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (index in 0 until state.indexCount) {
            key(index) {
                IndexItem(
                    color = { state.getColorStatusFromAnswer(selectedAnswers()[index], colors) },
                    isCurrent = { state.isCurrent(index) },
                    onIndexClick = { onIndexClick(index) }
                ) {
                    val updatedAlpha by animateFloatAsState(
                        if (state.isNotAnswered(
                                answerState = selectedAnswers()[index],
                                index = index
                            )
                        ) 0.5f else 1f
                    )
                    Text(
                        "${index + 1}",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .graphicsLayer {
                                alpha = updatedAlpha
                            },
                    )
                }
            }
        }
    }
}

/**
 * Creates and remembers a [ProgressTableState] instance.
 *
 * This function provides a convenient way to create a [ProgressTableState] and automatically
 * manage its state across recompositions and configuration changes. It leverages
 * [rememberSaveable] to preserve the state even when the activity or process is destroyed
 * and recreated.
 *
 * @param indexCount A lambda that returns the total number of indices in the progress table.
 *   This value determines the number of items displayed in the table.
 * @param currentIndex A lambda that returns the currently selected index in the progress table.
 *   This value indicates which item is considered the current one.
 * @return A remembered [ProgressTableState] instance.
 */
@Composable
fun rememberProgressTableState(indexCount: () -> Int, currentIndex: () -> Int) =
    rememberSaveable(saver = ProgressTableState.Saver) {
        ProgressTableState(indexCount, currentIndex)
    }.apply {
        currentIndexState.value = currentIndex
        indexCountState.value = indexCount
    }

/**
 * Represents the state of a progress table, managing the current index,
 * the total index count, and providing utility methods for determining the visual
 * representation of each index item.
 *
 * @property updatedIndexCount A lambda that returns the current total number of indices.
 * @property updatedCurrentIndex A lambda that returns the current index.
 */
class ProgressTableState(
    updatedIndexCount: () -> Int,
    updatedCurrentIndex: () -> Int,
) {
    var currentIndexState = mutableStateOf(updatedCurrentIndex)
        private set
    val currentIndex: Int
        get() = currentIndexState.value.invoke()

    var indexCountState = mutableStateOf(updatedIndexCount)
        private set
    val indexCount: Int
        get() = indexCountState.value.invoke()

    fun getColorStatusFromAnswer(
        answerState: IndexAnswerState?,
        colors: ProgressTableColors
    ): IndexItemStatusColor {
        val statusColor = when (answerState) {
            IndexAnswerState.Correct -> colors.correctContainer
            IndexAnswerState.Incorrect -> colors.incorrectContainer
            null -> colors.container
        }

        return IndexItemStatusColor(
            current = colors.currentBorderColor, status = statusColor
        )
    }

    fun isNotAnswered(
        answerState: IndexAnswerState?,
        index: Int,
    ): Boolean {
        return answerState == null && index != currentIndex
    }

    fun isCurrent(index: Int) = index == currentIndex

    companion object {
        val Saver: Saver<ProgressTableState, *> = listSaver(
            save = { listOf(it.indexCount, it.currentIndex) },
            restore = { ProgressTableState({ it[0] }) { it[1] } }
        )
    }
}

/**
 * Represents the color scheme for the [ProgressTable] component.
 *
 * @property correctContainer The color of the container for correctly answered questions.
 * @property incorrectContainer The color of the container for incorrectly answered questions.
 * @property currentBorderColor The border color for the currently selected question.
 * @property container The default color of the container for unanswered questions or the background of the cell.
 * @property content The color of the text content within the container.
 */
@Immutable
class ProgressTableColors(
    val correctContainer: Color,
    val incorrectContainer: Color,
    val currentBorderColor: Color,
    val container: Color,
    val content: Color,
) {
    fun copy(
        correctContainer: Color = this.correctContainer,
        incorrectContainer: Color = this.incorrectContainer,
        currentBorderColor: Color = this.currentBorderColor,
        containerColor: Color = this.container,
    ) = ProgressTableColors(
        correctContainer = correctContainer.takeOrElse { this.correctContainer },
        incorrectContainer = incorrectContainer.takeOrElse { this.incorrectContainer },
        currentBorderColor = currentBorderColor.takeOrElse { this.currentBorderColor },
        container = containerColor.takeOrElse { this.container },
        content = content.takeOrElse { this.content },
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ProgressTableColors

        if (correctContainer != other.correctContainer) return false
        if (incorrectContainer != other.incorrectContainer) return false
        if (currentBorderColor != other.currentBorderColor) return false
        if (container != other.container) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = correctContainer.hashCode()
        result = 31 * result + incorrectContainer.hashCode()
        result = 31 * result + currentBorderColor.hashCode()
        result = 31 * result + container.hashCode()
        return result
    }
}

/**
 * Contains the default values used for [ProgressTable].
 */
object ProgressTableDefaults {
    @Composable
    fun progressTableColors() = ProgressTableColors(
        correctContainer = MaterialTheme.colorScheme.primaryContainer,
        incorrectContainer = MaterialTheme.colorScheme.errorContainer,
        currentBorderColor = MaterialTheme.colorScheme.tertiary,
        container = Color.Transparent,
        content = MaterialTheme.colorScheme.onBackground,
    )
}


@PreviewLightDark
@Composable
private fun ProgressTablePreview() {
    KesiTheme {
        val indexCount = 5
        val currentIndex = 3
        val selectedAnswers = mapOf(
            0 to IndexAnswerState.Correct,
            1 to IndexAnswerState.Incorrect,
            2 to IndexAnswerState.Correct
        )
        ProgressTable(
            state = rememberProgressTableState({ indexCount }) { currentIndex },
            selectedAnswers = { selectedAnswers },
            onIndexClick = { }
        )
    }
}