package com.kesicollection.feature.article.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.kesicollection.core.uisystem.component.ContentType
import com.kesicollection.core.uisystem.component.DisplayContent
import com.kesicollection.core.uisystem.theme.KesiTheme
import java.util.UUID

/**
 * Represents a sub-header content type for display within an article or similar context.
 *
 * This class encapsulates the data and rendering logic for a sub-header, which is typically
 * used to break down content into logical sections.
 *
 * @sample com.kesicollection.feature.article.components.SubHeaderExample
 *
 * @property content The text content of the sub-header.
 * @property uiId A unique identifier for the sub-header, defaulting to a randomly generated UUID.
 */
class SubHeader(
    private val content: String,
    override val uiId: String = UUID.randomUUID().toString()
) : ContentType {
    override val type: String
        get() = this::class.simpleName!!

    @Composable
    override fun Content(modifier: Modifier) {
        SubHeaderContent(
            modifier = modifier,
            content = content
        )
    }
}

/**
 * Composable function to display a sub-header.
 *
 * This function renders a text element styled as a sub-header, typically used to divide
 * sections within a larger piece of content. It uses the `titleLarge` typography from
 * the Material Theme, with a bold font weight, and applies the `onBackground` color.
 *
 * @param content The text content to be displayed as the sub-header.
 * @param modifier Modifier to be applied to the text element.
 */
@Composable
fun SubHeaderContent(
    content: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = content,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground
    )
}

@PreviewLightDark
@Composable
private fun SubHeaderPreview() {
    SubHeaderExample()
}


@Composable
private fun SubHeaderExample(
    content: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
    modifier: Modifier = Modifier
) {
    KesiTheme {
        Column {
            DisplayContent(SubHeader(content), modifier = modifier)
        }
    }
}

