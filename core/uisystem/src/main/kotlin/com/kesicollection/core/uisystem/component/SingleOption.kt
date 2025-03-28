package com.kesicollection.core.uisystem.component

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.uisystem.component.SingleOptionDefaults.colorAnimation
import com.kesicollection.core.uisystem.component.SingleOptionDefaults.trailingScaleAnimation
import com.kesicollection.core.uisystem.theme.KesiTheme

/**
 * This composable renders content within a rounded outline. You can add a [trailing] element,
 * animate its size using [trailingScale] (between 0 and 1), and trailing element's color using the
 * [color] state.
 *
 * @sample com.kesicollection.core.uisystem.component.PreviewSingleOption
 *
 * @param modifier Modifier to be applied to the Card composable.
 * @param trailing A composable to be displayed at the trailing end of the option. Typically used for icons or indicators.
 * @param color A lambda that provides the color for the outline and trailing composable. This allows for dynamic color changes.
 * @param trailingScale A lambda that provides the scale factor (0f to 1f) for the trailing composable, enabling animated scaling.
 * @param onClick An optional lambda to be invoked when the option is clicked. If null, the option is not clickable.
 * @param content The main content of the option, displayed within the Card.
 *
 * @see [colorAnimation]
 * @see [trailingScaleAnimation]
 * @see [AnsweredOption]
 */
@Composable
fun SingleOption(
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit = {},
    color: () -> Color = { Color.Unspecified },
//    trailingScale: () -> Float = { 1f },
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Log.d("Andres", "QuestionCard: QUEPEDO!!!")
    val shape = MaterialTheme.shapes.large
    val currentDensity = LocalDensity.current

    val clickModifier = onClick.takeIf { true }?.let {
        Modifier
            .clip(RoundedCornerShape(shape.topStart))
            .clickable {
                it()
            }
    } ?: Modifier

    Card(
        modifier = clickModifier
            .testTag(":core:uisystem:singleOptionCard")
            .drawWithContent {
                drawContent()
                drawRoundRect(
                    cornerRadius = CornerRadius(shape.topStart.toPx(size, currentDensity)),
                    color = color(),
                    topLeft = Offset(0f, 0f),
                    style = Stroke(4.dp.toPx()),
                    size = size
                )
            }
            .then(modifier), shape = shape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                content()
            }

            trailing()
        }
    }
}

/**
 * Contains the default values used by [SingleOption]
 */
object SingleOptionDefaults {


    /**
     * Creates a scale animation for a trailing element.
     *
     * This composable function generates a `State<Float>` that represents a scale animation.
     * The animation scales the element from 0f to 1f when [isScale] is true, and
     * from 1f to 0f when [isScale] is false, using a spring-based animation.
     *
     * The animation uses different spring configurations based on the [isScale] state:
     * - When [isScale] is true, it uses a medium bouncy spring for a more pronounced effect.
     * - When [isScale] is false, it uses a no bouncy spring for a more immediate transition.
     *
     * @param isScale Determines whether the element must be scaled or no. If true, the element scales up;
     * if false, it scales down. Defaults to true.
     * @return A `State<Float>` representing the animated scale value.
     *
     * Example usage:
     * ```kotlin
     * @Composable
     * fun MyComposable(isSelected: Boolean) {
     *      val scale by trailingScaleAnimation(isScaled)
     *
     *      Box(
     *          modifier = Modifier
     *          .scale(scale)
     *          .background(Color.Blue)
     *          .size(50.dp)
     *      )
     * }
     * ```
     *
     * In this example, the `Box` will scale up or down based on the [isScale] state,
     * using the animation provided by `trailingScaleAnimation`.
     */
    @Composable
    fun trailingScaleAnimation(isScale: Boolean = true) = animateFloatAsState(
        targetValue = if (isScale) 1f else 0f,
        animationSpec = SpringSpec(
            dampingRatio = if (isScale) Spring.DampingRatioMediumBouncy else Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        )
    )

    /**
     * Animates the color of a composable based on the provided [AnsweredOption].
     *
     * This composable function generates a `State<Color>` that represents a color animation.
     * The animation transitions from the current color to a new color based on the
     * [AnsweredOption] provided. The animation uses a tween animation with a duration of
     * 150 milliseconds and an EaseIn easing function for a smooth transition.
     *
     * The color is determined by the following mapping:
     * - [AnsweredOption.CORRECT] : `MaterialTheme.colorScheme.primary`
     * - [AnsweredOption.INCORRECT] : `MaterialTheme.colorScheme.error`
     * - [AnsweredOption.PARTIALLY_CORRECT] : `MaterialTheme.colorScheme.tertiary`
     * - [AnsweredOption.DEFAULT] : `MaterialTheme.colorScheme.outline`
     *
     * @param option The [AnsweredOption] that determines the target color.
     * @return A `State<Color>` representing the animated color value.
     *
     * Example usage:
     * ```kotlin
     * @Composable
     * fun MyComposable(answeredOption: AnsweredOption) {
     *      val animatedColor by colorAnimation(answeredOption)
     *
     *      Box(
     *          modifier = Modifier
     *          .background(animatedColor)
     *          .size(50.dp)
     *      )
     * }
     * ```
     *
     * In this example, the `Box` will animate its background color based on the
     * `answeredOption` provided to `MyComposable`.
     */
    @Composable
    fun colorAnimation(option: AnsweredOption) = animateColorAsState(
        when (option) {
            AnsweredOption.CORRECT -> MaterialTheme.colorScheme.primary
            AnsweredOption.INCORRECT -> MaterialTheme.colorScheme.error
            AnsweredOption.PARTIALLY_CORRECT -> MaterialTheme.colorScheme.tertiary
            AnsweredOption.DEFAULT -> MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(150, easing = EaseIn)
    )
}

/**
 * Enum class representing the possible states of an answered option.
 *
 * This enum defines the different outcomes of an answer, such as being correct,
 * incorrect, partially correct, or the default state when no answer has been given.
 */
enum class AnsweredOption {
    /**
     * The answer is completely correct.
     */
    CORRECT,

    /**
     * The answer is completely incorrect.
     */
    INCORRECT,

    /**
     * The answer is partially correct.
     */
    PARTIALLY_CORRECT,

    /**
     * The default state when no answer has been given or the state is unknown.
     */
    DEFAULT
}

@PreviewLightDark
@Composable
private fun LightDarkSingleAnswerOption() {
    ExampleSingleOption()
}

@Composable
private fun ExampleSingleOption(modifier: Modifier = Modifier) {
    val rotation = listOf(
        AnsweredOption.DEFAULT,
        AnsweredOption.CORRECT,
        AnsweredOption.INCORRECT,
        AnsweredOption.PARTIALLY_CORRECT,
    )

    KesiTheme {
        var answeredOptionIndex by remember { mutableIntStateOf(0) }
        val acolor by colorAnimation(rotation[answeredOptionIndex])
        val scale by trailingScaleAnimation(answeredOptionIndex != 0)

        Column(
            modifier = modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SingleOption(
                modifier = Modifier.fillMaxWidth(),
                trailing = {
                    Text("Passed")
                },
//                trailingScale = { scale },
                onClick = {
                    answeredOptionIndex = (answeredOptionIndex + 1) % rotation.size
                },
                color = { acolor },
            ) {
                Text("List whatever text you want here...")
            }
        }
    }
}