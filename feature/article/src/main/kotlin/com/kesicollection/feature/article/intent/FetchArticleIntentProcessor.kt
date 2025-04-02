package com.kesicollection.feature.article.intent

import com.kesicollection.core.uisystem.component.ContentType
import com.kesicollection.feature.article.Reducer
import com.kesicollection.feature.article.components.BulletList
import com.kesicollection.feature.article.components.Code
import com.kesicollection.feature.article.components.Paragraph
import com.kesicollection.feature.article.components.SubHeader
import kotlinx.coroutines.delay

class FetchArticleIntentProcessor(
    private val articleId: String,
) : IntentProcessor {
    override suspend fun processIntent(reducer: (Reducer) -> Unit) {
        reducer {
            copy(
                isLoading = false,
                title = "This is the loaded content $articleId",
                content = testData
            )
        }
    }
}

val lorem =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

val testData = listOf<ContentType>(
    SubHeader("Subheader example with more data to test"),
    Paragraph(lorem.take(100)),
    BulletList(
        "Bullet list example with more data to test",
        listOf(lorem.take(150), lorem, lorem.take(80), lorem.take(30), lorem)
    ),
    Code("""
```
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
        })
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
```
        """.trimIndent()),
    Paragraph(lorem),
)