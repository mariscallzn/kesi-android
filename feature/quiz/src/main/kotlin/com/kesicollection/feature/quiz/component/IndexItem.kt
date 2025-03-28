package com.kesicollection.feature.quiz.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.uisystem.theme.KesiTheme

/**
 * A composable function that displays an index item, typically used to represent a step or item in a sequence.
 *
 * The item is displayed as a circle with a background color representing its status, and an optional border
 * indicating if it's the currently active item. It also contains custom content provided via the `content` lambda.
 *
 * @sample com.kesicollection.feature.quiz.component.ExampleIndexItem
 *
 * @param color A lambda that provides the [IndexItemStatusColor] based on the current status of the item.
 *   - `status`: The color representing the status of the index item (e.g., correct, incorrect, unseen).
 *   - `current`: The color used for the border when `isCurrent` is true, indicating it's the active item.
 * @param isCurrent A lambda that returns `true` if this index item is the currently active one, `false` otherwise.
 * @param onIndexClick A lambda invoked when the index item is clicked.
 * @param modifier The [Modifier] to be applied to the index item's container.
 * @param content The composable content to be displayed inside the index item's circle. This lambda
 *   has a [BoxScope] receiver, allowing the content to be positioned using alignment.
 */
@Composable
fun IndexItem(
    color: () -> IndexItemStatusColor,
    isCurrent: () -> Boolean,
    onIndexClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(32.dp)
            .clickable { onIndexClick() }
            .drawWithContent {
                drawCircle(color = color().status)
                if (isCurrent()) {
                    drawCircle(
                        color = color().current,
                        style = Stroke(1.dp.toPx())
                    )
                }
                drawContent()
            }) { content() }
}

/**
 * Represents the colors used to indicate the status of an index item in a quiz.
 *
 * @property current The color used to indicate that the item is the currently selected one.
 *                   Usually a border color when isCurrent is true.
 * @property status The background color representing the status of the index item.
 *                  For example, green for correct, red for incorrect, or transparent for unseen.
 */
data class IndexItemStatusColor(
    val current: Color,
    val status: Color
)

@PreviewLightDark
@Composable
private fun PreviewIndexItemCorrectStatus_Current() {
    KesiTheme {
        val color = MaterialTheme.colorScheme.primaryContainer
        val current = MaterialTheme.colorScheme.tertiary
        ExampleIndexItem(
            modifier = Modifier.padding(8.dp),
            statusColor = color,
            currentColor = current,
            isCurrent = true
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewIndexItemIncorrectStatus_Current() {
    KesiTheme {
        val color = MaterialTheme.colorScheme.errorContainer
        val current = MaterialTheme.colorScheme.tertiary
        ExampleIndexItem(
            modifier = Modifier.padding(8.dp),
            statusColor = color,
            currentColor = current,
            isCurrent = true
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewIndexItemCorrectStatus() {
    KesiTheme {
        val color = MaterialTheme.colorScheme.primaryContainer
        ExampleIndexItem(
            modifier = Modifier.padding(8.dp),
            statusColor = color,
            isCurrent = true
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewIndexItemIncorrectStatus() {
    KesiTheme {
        val color = MaterialTheme.colorScheme.errorContainer
        ExampleIndexItem(
            modifier = Modifier.padding(8.dp),
            statusColor = color,
            isCurrent = true
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewIndexItemUnseenStatus() {
    KesiTheme {
        val color = Color.Transparent
        ExampleIndexItem(
            modifier = Modifier.padding(8.dp),
            statusColor = color,
            isCurrent = true
        )
    }
}

@Composable
private fun ExampleIndexItem(
    statusColor: Color,
    currentColor: Color = Color.Transparent,
    isCurrent: Boolean,
    modifier: Modifier = Modifier,
) {
    IndexItem(
        modifier = modifier,
        onIndexClick = {},
        color = {
            IndexItemStatusColor(
                status = statusColor,
                current = currentColor
            )
        },
        isCurrent = { isCurrent }
    ) {
        Text(
            "1",
            modifier = Modifier.align(
                Alignment.Center,
            )
        )
    }
}