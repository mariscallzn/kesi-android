package com.kesicollection.articles.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kesicollection.articles.LocalImageLoader
import com.kesicollection.articles.model.UiArticle
import com.kesicollection.core.uisystem.theme.KesiTheme

@Composable
fun Article(
    article: UiArticle,
    modifier: Modifier = Modifier,
    onArticleClick: (UiArticle) -> Unit = {},
) {
    Row(
        modifier = modifier.then(
            Modifier
                .clickable { onArticleClick(article) }
                .padding(horizontal = 16.dp, vertical = 24.dp)),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                article.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = article.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
        }
        AsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(5))
                .heightIn(max = 80.dp)
                .width(120.dp),
            contentScale = ContentScale.Fit,
            model =
                ImageRequest.Builder(LocalContext.current)
                    .data(article.thumbnail)
                    .crossfade(true)
                    .build(),
            imageLoader = LocalImageLoader.current,
            contentDescription = null
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewArticle() {
    ExampleArticle()
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ExampleArticle(
    article: UiArticle = UiArticle(
        title = "Modern Android development ",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet.",
        articleId = "1",
        thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
    ),
    modifier: Modifier = Modifier
) {
    KesiTheme {
        val imageColor = MaterialTheme.colorScheme.tertiaryContainer
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(imageColor.toArgb())
        }

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            CompositionLocalProvider(
                LocalImageLoader provides ImageLoader.Builder(
                    LocalContext.current
                ).build()
            ) {
                Article(
                    modifier = modifier,
                    article = article,
                )
            }
        }
    }
}