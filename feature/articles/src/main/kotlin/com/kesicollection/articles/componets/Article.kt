package com.kesicollection.articles.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kesicollection.articles.LocalImageLoader
import com.kesicollection.articles.model.UiArticle

@Composable
fun Article(
    article: UiArticle,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Column {
            Text(article.title)
            Text(article.description)
        }
        AsyncImage(
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

@Composable
private fun ExampleArticle(modifier: Modifier = Modifier) {
//    Article(modifier = modifier)
}