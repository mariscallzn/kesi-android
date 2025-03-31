package com.kesicollection.articles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.kesicollection.articles.componets.Article
import com.kesicollection.articles.model.UiArticle
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.articles.R
import dagger.hilt.EntryPoints

/**
 * Provides a [CompositionLocal] for accessing an [ImageLoader] instance.
 *
 * This `CompositionLocal` allows components within a composition to easily access and
 * utilize an [ImageLoader] for image loading operations. It defaults to throwing an error
 * if no [ImageLoader] is provided.
 *
 * You should provide an [ImageLoader] instance higher up in the composition tree using
 * `CompositionLocalProvider(LocalImageLoader provides myImageLoader) { ... }`.
 *
 * @throws IllegalStateException if accessed without a provided [ImageLoader].
 */
val LocalImageLoader = compositionLocalOf<ImageLoader> {
    error("")
}

@Composable
fun ArticlesScreen(
    modifier: Modifier = Modifier,
    onArticleClick: (articleId: String) -> Unit,
    viewModel: ArticlesViewModel = hiltViewModel()
) {
    val imageLoader = EntryPoints.get(
        LocalContext.current.applicationContext,
        ImageLoaderEntryPoint::class.java
    ).imageLoader()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(Intent.FetchArticles)
    }

    CompositionLocalProvider(value = LocalImageLoader provides imageLoader) {
        ArticlesScreen(
            modifier = modifier,
            onArticleClick = onArticleClick,
            uiState = uiState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArticlesScreen(
    uiState: UiArticlesState,
    onArticleClick: (articleId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    KScaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = {
                Text(stringResource(R.string.feature_articles_topbar_title))
            })
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            itemsIndexed(uiState.articles, key = { _, item -> item.articleId }) { index, item ->
                Article(article = item, onArticleClick = { onArticleClick(it.articleId) })
                if (index < uiState.articles.size - 1) {
                    Box(
                        Modifier
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                            .height(1.dp)
                            .fillParentMaxWidth()
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ArticlesScreenPreview() {
    ArticlesScreenExample()
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ArticlesScreenExample(
    uiState: UiArticlesState = UiArticlesState(
        articles = listOf(
            UiArticle(
                title = "Modern Android development ",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet.",
                articleId = "1",
                thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
            ),
            UiArticle(
                title = "Modern Android development ",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet.",
                articleId = "2",
                thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
            )
        )
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
                ArticlesScreen(
                    modifier = modifier,
                    uiState = uiState,
                    onArticleClick = {}
                )
            }
        }
    }
}