package com.kesicollection.feature.article.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.kesicollection.core.uisystem.component.ContentType
import com.kesicollection.core.uisystem.component.DisplayContent
import com.kesicollection.core.uisystem.theme.KesiTheme

class Paragraph(val content: String) : ContentType {
    @Composable
    override fun Content(modifier: Modifier) {
        Paragraph(
            modifier = modifier,
            content = content
        )
    }
}

@Composable
fun Paragraph(
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
        DisplayContent(Paragraph(content), modifier = modifier)
    }
}