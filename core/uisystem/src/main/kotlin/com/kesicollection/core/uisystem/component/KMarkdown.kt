package com.kesicollection.core.uisystem.component

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.kesicollection.core.uisystem.KotlinGrammarLocator
import com.kesicollection.core.uisystem.R
import com.kesicollection.core.uisystem.theme.KesiTheme
import io.noties.markwon.Markwon
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j

/**
 * Composable function to display Markdown text.
 *
 * This function uses the Markwon library to render Markdown content within an AndroidView.
 * It supports syntax highlighting for Kotlin code blocks using Prism4j.
 * The text color, size, and font are styled based on the MaterialTheme.
 *
 * @param text The Markdown string to display.
 * @param modifier Optional [Modifier] to be applied to the composable.
 */
@Composable
fun KMarkdown(
    text: String,
    modifier: Modifier = Modifier
) {
    // Get the current context.
    val context = LocalContext.current

    // Initialize Markwon with syntax highlighting for Kotlin.
    val markwon = Markwon.builder(context)
        // Use the SyntaxHighlightPlugin with Prism4j for Kotlin.
        // KotlinGrammarLocator provides the grammar for Kotlin.
        .usePlugin(
            SyntaxHighlightPlugin.create(
                Prism4j(KotlinGrammarLocator()), Prism4jThemeDarkula.create()
            )
        )
        .build()

    // Get the primary text color from the MaterialTheme.
    val m3PrimaryColor = MaterialTheme.colorScheme.onBackground
    // Get the body large text style from the MaterialTheme.
    val m3BodyLargeTextStyle = MaterialTheme.typography.bodyLarge

    // Use AndroidView to embed the TextView that will display the Markdown.
    AndroidView(
        modifier = modifier,
        // Factory function to create the TextView.
        factory = { ctx ->
            TextView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                // Set the text color from the MaterialTheme.
                setTextColor(m3PrimaryColor.toArgb())
                // Set the text size from the MaterialTheme.
                textSize = m3BodyLargeTextStyle.fontSize.value
                // Set the typeface. Attempt to load a custom font, fallback to default.
                typeface = m3BodyLargeTextStyle.fontFamily?.let {
                    try {
                        // Attempt to load the custom font 'lexend_regular'.
                        ResourcesCompat.getFont(context, R.font.lexend_regular)
                    } catch (e: Exception) {
                        Typeface.DEFAULT // Fallback to the default typeface if the custom font fails to load.
                    }
                } ?: Typeface.DEFAULT // Fallback to default if fontFamily is null.
            }
        },
        // Update function to apply changes to the TextView when the input 'text' or theme changes.
        update = { textView ->
            // Re-apply text color in case of theme changes.
            textView.setTextColor(m3PrimaryColor.toArgb())
            // Re-apply text size in case of theme changes.
            textView.textSize = m3BodyLargeTextStyle.fontSize.value
            // Set the Markdown content to the TextView using Markwon.
            markwon.setMarkdown(textView, text)
        }
    )
}


@Composable
/**
 * Private composable function to demonstrate the usage of [KMarkdown].
 * This is typically used for previews or internal examples.
 *
 * @param modifier Optional [Modifier] to be applied to the [KMarkdown] composable.
 * @param text The Markdown string to display. Defaults to [codeExample].
 */
private fun ExampleKMarkdown(
    modifier: Modifier = Modifier,
    text: String = codeExample,
) {
    KMarkdown(
        text = text,
        modifier = modifier
    )
}

@PreviewLightDark
/**
 * Private composable function for previewing [ExampleKMarkdown] in both light and dark themes.
 */
@Composable
private fun PreviewKMarkdown() {
    KesiTheme {
        ExampleKMarkdown()
    }
}

/**
 * A constant string containing an example Markdown content with a Kotlin code block.
 */
const val codeExample = """
## Code

```kotlin
class KotlinGrammarLocator : GrammarLocator {
    override fun grammar(
        prism4j: Prism4j,
        language: String
    ): Prism4j.Grammar? = PrismKotlin.create()

    override fun languages(): Set<String?> =
        setOf("kotlin")
}
```
---
"""