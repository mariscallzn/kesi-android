package com.kesicollection.feature.article.components

import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kesicollection.core.uisystem.component.ContentType
import com.kesicollection.core.uisystem.component.DisplayContent
import com.kesicollection.core.uisystem.theme.KesiTheme
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme

/**
 * Represents a code block content type for displaying code snippets.
 *
 * @param content The code content to be displayed.
 */
class Code(val content: String) : ContentType {
    @Composable
    override fun Content(modifier: Modifier) {
        Code(
            modifier = modifier,
            content = content
        )
    }
}

/**
 * Composable function to display code blocks with syntax highlighting.
 *
 * **NOTE: This code has room for improvements but for the sake of speed we'll keep it like this
 * That is why this component it's localized with in this module and not in the `:uisystem`**
 *
 * @sample com.kesicollection.feature.article.components.CodeExample
 *
 * @param content The code content to be displayed.
 * @param modifier Modifier to apply to the code block container.
 */
@Composable
fun Code(
    content: String,
    modifier: Modifier = Modifier
) {
    val codeColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val codeBlockBackground = if (isSystemInDarkTheme())
        MaterialTheme.colorScheme.background.copy(0.4f)
            .toArgb() else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f).toArgb()

    AndroidView(
        modifier = modifier
            .clip(RoundedCornerShape(5))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(5)
            )
            .horizontalScroll(
                rememberScrollState()
            ),
        factory = { context ->
            val markwon = Markwon.builder(context)
                .usePlugin(object : AbstractMarkwonPlugin() {
                    override fun configureTheme(builder: MarkwonTheme.Builder) {
                        builder
                            .codeTextColor(codeColor)
                            .codeBackgroundColor(codeBlockBackground)
                    }
                })
                .build()
            TextView(context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                setTextColor(codeColor)
                markwon.setMarkdown(this, content)
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun CodePreview() {
    CodeExample(
        modifier = Modifier
            .padding(8.dp)
            .height(110.dp)
    )
}


@Composable
private fun CodeExample(
    content: String = """
```
val textView = TextView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}                        
```
        """.trimIndent(),
    modifier: Modifier = Modifier
) {
    KesiTheme {
        DisplayContent(Code(content), modifier = modifier)
    }
}