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
import java.util.UUID


/**
 * Represents a code block content type.
 *
 * This class encapsulates a string of code and provides a composable function to display it
 * within the UI. It implements the [ContentType] interface and is designed to be used with
 * the `DisplayContent` composable.
 *
 * @sample com.kesicollection.feature.article.components.CodeExample
 *
 * @param content The string content of the code block.
 * @param uiId A unique identifier for this code block instance. Defaults to a newly generated UUID.
 */
class Code(
    private val content: String,
    override val uiId: String = UUID.randomUUID().toString()
) : ContentType {
    override val type: String
        get() = this::class.simpleName!!

    @Composable
    override fun Content(modifier: Modifier) {
        CodeContent(
            modifier = modifier,
            content = content
        )
    }
}


/**
 * Composable function to display code content within a styled code block.
 *
 * This function takes a string of code as input and displays it within a styled
 * code block using the `Markwon` library for Markdown rendering. The code block
 * is designed to be visually distinct, with customizable colors based on the
 * current system theme (light or dark).
 *
 * @param content The string content of the code to be displayed.
 * @param modifier Modifier to apply to the code block container. This allows for
 *  customization of the layout and appearance of the code block. By default it uses
 *  a horizontal scroll and rounded corners.
 */
@Composable
fun CodeContent(
    content: String,
    modifier: Modifier = Modifier
) {
    val codeColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val codeBlockBackground = if (isSystemInDarkTheme())
        MaterialTheme.colorScheme.background.copy(0.4f)
            .toArgb() else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f).toArgb()

    AndroidView(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(5.dp)
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
        DisplayContent(Code(content = content), modifier = modifier)
    }
}