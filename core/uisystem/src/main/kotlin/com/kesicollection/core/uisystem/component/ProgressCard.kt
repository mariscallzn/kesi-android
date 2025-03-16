package com.kesicollection.core.uisystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme

/**
 * A composable function that creates an expandable card displaying progress.
 *
 * This card shows a linear progress indicator along with optional current index and total values.
 * It can be expanded to reveal additional content.
 *
 * The card displays a linear progress bar, a current index, and a total value (if provided).
 * An expand/collapse icon allows the user to show or hide additional content.
 * The card animates its size when expanded or collapsed.
 *
 * @sample com.kesicollection.core.uisystem.component.ExampleExpandableProgressCard
 *
 * @param progress A lambda function that returns the current progress value as a Float (0.0 to 1.0).
 *                 This determines the progress shown on the LinearProgressIndicator.
 * @param onExpandedClick A lambda function to be executed when the expand/collapse icon is clicked.
 *                        This should typically update the state of the `state` parameter.
 * @param modifier Modifier to be applied to the card.
 *                 This allows customization of the card's appearance and layout.
 * @param state A lambda function that returns a Boolean indicating whether the card is expanded (true) or collapsed (false).
 *              Defaults to collapsed. This controls the visibility of the `content` and the rotation of the expand */
@Composable
fun ExpandableProgressCard(
    progress: () -> Float,
    onExpandedClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: () -> Boolean = { false },
    currentIndex: @Composable () -> Unit = {},
    total: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.animateContentSize(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            val rotation by animateFloatAsState(if (state()) 180f else 0f)

            currentIndex()
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .weight(1f)
            )
            total()
            IconButton(onClick = onExpandedClick) {
                Icon(
                    imageVector = KIcon.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                )
            }
        }
        AnimatedVisibility(state()) { content() }
    }
}

@PreviewLightDark
@Composable
private fun PreviewLightDarkProgressCard() {
    ExampleExpandableProgressCard(modifier = Modifier.padding(8.dp))
}

@Preview
@Composable
private fun PreviewProgressCard() {
    ExampleExpandableProgressCard(modifier = Modifier.padding(8.dp))
}

@Composable
private fun ExampleExpandableProgressCard(modifier: Modifier = Modifier) {
    KesiTheme {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        ExpandableProgressCard(
            modifier = modifier,
            onExpandedClick = { isExpanded = !isExpanded },
            state = { isExpanded },
            progress = { 1f },
            currentIndex = { Text("1") },
            total = { Text("30") }
        ) {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                color = MaterialTheme.colorScheme.inverseSurface,
            ) {
                Text(
                    "Put your content here!",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}