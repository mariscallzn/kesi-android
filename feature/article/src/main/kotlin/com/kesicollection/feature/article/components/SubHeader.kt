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

class SubHeader(private val content: String) : ContentType {
    @Composable
    override fun Content(modifier: Modifier) {
        SubHeaderContent(
            modifier = modifier,
            content = content
        )
    }
}

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

