package com.kesicollection.feature.article.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.kesicollection.core.uisystem.component.ContentType
import com.kesicollection.core.uisystem.component.DisplayContent
import com.kesicollection.core.uisystem.theme.KesiTheme
import java.util.UUID

/**
 * Represents a paragraph of text content within an article.
 *
 * This class encapsulates the text content of a paragraph and provides a composable function
 * to render it within a UI. It also implements the [ContentType] interface, allowing it to
 * be used within a larger content structure.
 *
 * @sample com.kesicollection.feature.article.components.ParagraphExample
 *
 * @property content The text content of the paragraph.
 * @property uiId A unique identifier for this paragraph within the UI. Defaults to a randomly generated UUID.
 */
class Paragraph(
    private val content: String,
    override val uiId: String = UUID.randomUUID().toString()
) : ContentType {
    override val type: String
        get() = this::class.simpleName!!

    @Composable
    override fun Content(modifier: Modifier) {
        ParagraphContent(
            modifier = modifier,
            content = content
        )
    }
}

/**
 * Composable function to display the text content of a paragraph.
 *
 * This function renders the provided [content] as a paragraph of text, applying the
 * `bodyLarge` text style from the MaterialTheme typography.
 *
 * @param content The text content to be displayed within the paragraph.
 * @param modifier Modifier to be applied to the text.
 */
@Composable
fun ParagraphContent(
    content: String,
    modifier: Modifier = Modifier
) {
    Text(text = content, modifier = modifier, style = MaterialTheme.typography.bodyLarge)
}

@PreviewLightDark
@Composable
private fun ParagraphPreview() {
    ParagraphExample(
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur adipiscing elit. Ddolore magna aliqua, consectetur adipiscing elit."
    )
}

@Composable
private fun ParagraphExample(
    content: String = "",
    modifier: Modifier = Modifier
) {
    KesiTheme {
        DisplayContent(Paragraph(content = content), modifier = modifier)
    }
}