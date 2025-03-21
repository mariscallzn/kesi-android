package com.kesicollection.core.uisystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

/**
 * A composable function that provides a styled card layout.
 *
 * **This function is a thin wrapper around the Material 3 [Card] composable.** It
 * simplifies the process of creating cards by providing default values and
 * making it easy to customize common card attributes. It offers a convenient way
 * to create cards with pre-defined styling options.
 * It allows customization of the card's shape, colors, elevation, and border,
 * as well as its content.
 *
 * @param modifier The [Modifier] to be applied to this card.
 * @param shape The shape of the card. Defaults to [CardDefaults.shape].
 * @param colors The colors to be used for the card. Defaults to [CardDefaults.cardColors].
 * @param elevation The elevation of the card. Defaults to [CardDefaults.cardElevation].
 * @param border Optional border to draw around the card. Defaults to `null` (no border).
 * @param content The content of the card, defined within a [ColumnScope].
 *
 * Example Usage:
 * ```
 * KCard(
 *     modifier = Modifier.padding(16.dp),
 *     shape = RoundedCornerShape(8.dp),
 *     colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
 *     elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
 *     border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
 * ) {
 *     Text("Card Title", modifier = Modifier.padding(16.dp))
 *     Text("Card Content", modifier = Modifier.padding(horizontal = 1
 *   }
 * ```
 */
@Composable
fun KCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        content = content,
    )
}