package com.kesicollection.feature.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.uisystem.component.ContentType
import com.kesicollection.core.uisystem.component.DisplayContent
import com.kesicollection.core.uisystem.theme.KesiTheme
import java.util.UUID

/**
 * Represents a bulleted list content type.
 *
 * @sample com.kesicollection.feature.article.components.BulletListExample
 *
 * @property content The introductory content displayed above the bullet points.
 * @property bullets The list of bullet points to display.
 * @property uiId A unique identifier for this content instance. Defaults to a randomly generated UUID.
 */
class BulletList(
    val content: String,
    val bullets: List<String>,
    override val uiId: String = UUID.randomUUID().toString()
) : ContentType {
    override val type: String
        get() = this::class.simpleName!!

    @Composable
    override fun Content(modifier: Modifier) {
        BulletListContent(
            modifier = modifier,
            content = content,
            bullets = bullets,
        )
    }
}

/**
 * Composable function that displays a bulleted list.
 *
 * @param content The introductory text displayed above the list of bullets.
 * @param bullets The list of strings to be displayed as bullet points.
 * @param modifier Modifier to apply to the container of the bullet list.
 */
@Composable
fun BulletListContent(
    content: String,
    bullets: List<String>,
    modifier: Modifier = Modifier
) {
    Column {
        Text(text = content, modifier = modifier, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        bullets.forEach { bullet ->
            Row(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .size(4.dp)
                        .alignBy { it.measuredHeight + (24.dp.value / 2).toInt() }
                )
                Text(
                    bullet, modifier = Modifier
                        .alignByBaseline()
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@PreviewLightDark
@Composable
private fun BulletListPreview() {
    BulletListExample()
}


@Composable
private fun BulletListExample(
    content: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
    bullets: List<String> = listOf(
        "Lorem ipsum dolor sit amet",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, Lorem ipsum dolor sit amet, consectetur adipiscing elit",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, Lorem ipsum dolor sit amet, consectetur adipiscing elit, Lorem ipsum dolor sit amet, consectetur adipiscing elit",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
    ),
    modifier: Modifier = Modifier
) {
    KesiTheme {
        DisplayContent(
            contentType = BulletList(
                content = content,
                bullets = bullets
            ), modifier = modifier
        )
    }
}