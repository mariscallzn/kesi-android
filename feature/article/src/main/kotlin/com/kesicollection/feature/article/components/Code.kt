package com.kesicollection.feature.article.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.kesicollection.core.uisystem.component.ContentType
import com.kesicollection.core.uisystem.theme.KesiTheme

class Code(val content: String) : ContentType {
    @Composable
    override fun Content(modifier: Modifier) {
        Code(
            modifier = modifier,
            content = content
        )
    }
}

@Composable
fun Code(
    content: String,
    modifier: Modifier = Modifier
) {
    Text(text = content, modifier = modifier, style = MaterialTheme.typography.displayMedium)
}

@PreviewLightDark
@Composable
private fun CodePreview() {
    CodeExample()
}


@Composable
private fun CodeExample(
    content: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
    modifier: Modifier = Modifier
) {
    KesiTheme {
        Code(content = content, modifier = modifier)
    }
}