package com.kesicollection.core.uisystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Interface for defining a composable content type.
 *
 * This interface provides a standard way to define and render content within the UI system.
 * Implementations should provide a [Content] function that describes the composable UI to be rendered.
 *
 * @see Content
 */
interface ContentType {
    @Composable
    fun Content(modifier: Modifier)
}

/**
 * Displays the content associated with a given [ContentType].
 *
 * This composable acts as a dispatcher, rendering the appropriate UI based on the provided [contentType].
 * It delegates the actual rendering to the [ContentType.Content] function of the given type.
 *
 * @param contentType The [ContentType] whose content should be displayed.
 * @param modifier An optional [Modifier] to be applied to the content.
 */
@Composable
fun DisplayContent(
    contentType: ContentType,
    modifier: Modifier = Modifier
) {
    contentType.Content(modifier = modifier)
}