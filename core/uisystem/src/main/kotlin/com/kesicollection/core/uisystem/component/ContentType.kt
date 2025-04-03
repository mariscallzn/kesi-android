package com.kesicollection.core.uisystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


/**
 * Interface for defining a composable content type.
 *
 * This interface represents a distinct type of content that can be rendered within the UI.
 * Implementations are responsible for defining:
 * - [type]: A unique string identifier for the content type.
 * - [uiId]: A unique identifier used to distinguish instances of the same content type in the UI.
 * - [Content]: A composable function that renders the UI for this specific content type.
 *
 * The [DisplayContent] function uses this interface to display content dynamically based on the provided [ContentType].
 *
 * @property type A unique string identifier for this content type.
 * @property uiId A unique string identifier for this instance of the content type.
 * @see Content The composable function to render the content.
 * @see DisplayContent The composable function to display the content.
 */
interface ContentType {
    val type: String
    val uiId: String

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