package com.kesicollection.core.uisystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.uisystem.theme.KesiTheme

/**
 * A composable that overlays a vertical gradient from bottom to top over its content.
 *
 * This function creates a [Box] composable that displays the provided [content] and overlays
 * it with a vertical gradient. The gradient starts from the bottom and transitions to the top,
 * using the colors provided in the [colors] parameter.
 *
 * @sample com.kesicollection.core.uisystem.component.BottomTopVerticalGradientExample
 *
 * @param modifier The modifier to be applied to the container [Box].
 * @param colors The list of colors to use for the vertical gradient. Defaults to a gradient
 *   using [BottomTopVerticalGradientDefaults.bottomTopGradientColors], which is a gradient based on the
 *   surface color of the current [MaterialTheme].
 * @param content The composable content to be displayed under the gradient overlay.
 */
@Composable
fun BottomTopVerticalGradient(
    modifier: Modifier = Modifier,
    colors: List<Color> = BottomTopVerticalGradientDefaults.bottomTopGradientColors(),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        content()
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = colors
                    )
                )
        )
    }
}

/**
 * Default values for [BottomTopVerticalGradient].
 */
object BottomTopVerticalGradientDefaults {
    /**
     * Provides a default list of colors for a bottom-to-top vertical gradient.
     *
     * This function returns a list of three colors derived from the current [MaterialTheme]'s
     * surface color. The colors are configured to create a gradient effect, starting with a
     * semi-transparent surface color (alpha = 0.4f) at the bottom, transitioning to a slightly
     * less transparent surface color (alpha = 0.6f) in the middle, and finally ending with the
     * fully opaque surface color at the top.
     *
     * @return A [List] of [Color] representing the gradient colors.
     */
    @Composable
    fun bottomTopGradientColors() = listOf(
        MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surface,
    )
}

@PreviewLightDark
@Composable
private fun BottomTopVerticalGradientPreview() {
    BottomTopVerticalGradientExample(
        modifier = Modifier.wrapContentSize()
    )
}

@Composable
private fun BottomTopVerticalGradientExample(modifier: Modifier = Modifier) {
    KesiTheme {
        BottomTopVerticalGradient(modifier = modifier) {
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.tertiary)
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
    }
}