package com.kesicollection.articles

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import com.kesicollection.articles.componets.Article
import com.kesicollection.core.uisystem.component.KScaffold
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
            uiState = uiState,
        )
    }
}

@Composable
internal fun ArticlesScreen(
    uiState: UiArticlesState,
    modifier: Modifier = Modifier
) {
    KScaffold(modifier = modifier) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(uiState.articles, { it.articleId }) {
                Article(it)
            }
        }
    }
}