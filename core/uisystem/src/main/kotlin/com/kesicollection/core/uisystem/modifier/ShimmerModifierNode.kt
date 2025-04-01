package com.kesicollection.core.uisystem.modifier

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * A [DrawModifierNode] and [LayoutModifierNode] that implements a shimmer effect.
 *
 * This node creates a horizontal gradient brush that moves across the composable,
 * creating a shimmer or highlight effect.
 *
 * @sample com.kesicollection.core.uisystem.modifier.ShimmerExample
 *
 * @property paintColors The list of colors used to create the gradient brush.
 * @property brushSizeRatio The ratio of the brush size to the width of the composable.
 *   Must be a value between 0.0 (exclusive) and 1.0.
 */
private class ShimmerNode(
    var paintColors: List<Color>,
    @FloatRange(0.0, 1.0, toInclusive = false)
    var brushSizeRatio: Float = 0.9f
) : DrawModifierNode, LayoutModifierNode, Modifier.Node() {
    private var offset = Animatable(0f)
    private var size = IntSize.Zero
    var brushSize = 0f

    override fun onAttach() {
        super.onAttach()
        coroutineScope.launch {
            offset.animateTo(
                size.width.toFloat(),
                infiniteRepeatable(
                    tween(
                        1000,
                        easing = LinearEasing
                    ), RepeatMode.Restart
                )
            )
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val p = measurable.measure(constraints)
        size = IntSize(p.width, p.height)
        brushSize = size.width * brushSizeRatio
        offset = Animatable(-brushSize)
        return layout(p.width, p.height) {
            p.place(0, 0)
        }
    }

    override fun ContentDrawScope.draw() {
        val brush = Brush.horizontalGradient(
            colors = paintColors,
            startX = offset.value,
            endX = offset.value + brushSize,
            tileMode = TileMode.Clamp
        )
        drawRect(brush)
        drawContent()
    }
}

/**
 * Represents the configuration for the [ShimmerNode].
 *
 * This data class holds the necessary parameters to create and update a [ShimmerNode].
 *
 * @property colors The list of colors used to create the gradient brush.
 * @property brushSizeRatio The ratio of the brush size to the width of the composable.
 *   Must be a value between 0.0 (exclusive) and 1.0. Defaults to 0.9.
 */
@Stable
private data class ShimmerElement(
    var colors: List<Color>,
    @FloatRange(0.0, 1.0, toInclusive = false)
    var brushSizeRatio: Float = 0.9f
) : ModifierNodeElement<ShimmerNode>() {
    override fun create(): ShimmerNode = ShimmerNode(colors, brushSizeRatio)
    override fun update(node: ShimmerNode) {
        node.paintColors = colors
        node.brushSizeRatio = brushSizeRatio
    }
}

/**
 * Applies a shimmer animation to this [Modifier].
 *
 * @param colors The list of colors to use for the shimmer gradient. The gradient will be
 *   created horizontally, transitioning through the provided colors.
 * @return A [Modifier] with the shimmer animation applied.
 */
fun Modifier.animateShimmer(colors: List<Color>) = this then ShimmerElement(colors)

/**
 * Default values for the shimmer effect.
 */
object ShimmerModifierDefaults {

    /**
     * Provides a default list of colors for the shimmer effect.
     *
     * This list consists of surface container colors from the Material Theme, with varying alpha levels
     * to create a subtle shimmer effect.
     *
     * @return A list of [Color] objects representing the default shimmer colors.
     */
    @Composable
    fun defaultColorList() : List<Color> {
        return listOf(
            MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.4f),
            MaterialTheme.colorScheme.surfaceContainer,
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.4f),
            MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.2f),
        )
    }
}

@Preview
@Composable
private fun ShimmerExample() {
    Box(Modifier.safeContentPadding()) {
        Box(
            Modifier
                .clip(RoundedCornerShape(25))
                .height(150.dp)
                .fillMaxWidth()
                .animateShimmer(
                    listOf(
                        Color.White.copy(alpha = 0.6f),
                        Color.LightGray.copy(alpha = 0.6f),
                        Color.White.copy(alpha = 0.6f),
                    )
                )
        )
    }
}