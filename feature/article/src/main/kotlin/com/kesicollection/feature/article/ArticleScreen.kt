package com.kesicollection.feature.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.PreviewAppManager
import com.kesicollection.core.uisystem.component.BottomTopVerticalGradient
import com.kesicollection.core.uisystem.component.KAdView
import com.kesicollection.core.uisystem.component.KMarkdown
import com.kesicollection.core.uisystem.component.ShowError
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.article.components.LoadingArticle
import com.kesicollection.feature.article.components.PodcastCard
import com.kesicollection.feature.article.di.ArticleEntryPoint
import com.kesicollection.feature.article.uimodel.UiPodcast
import dagger.hilt.android.EntryPointAccessors

/**
 * Composable function that displays the Article screen.
 *
 * This function fetches the article data based on the provided [articleId]
 * and displays it using the [ArticleScreen] composable. It also handles
 * navigation up using the [onNavigateUp] callback.
 *
 * @param articleId The ID of the article to be displayed.
 * @param onNavigateUp Callback invoked when the user wants to navigate up.
 * @param viewModel The [ArticleViewModel] instance to use for managing the article data.
 *                  Defaults to a [hiltViewModel] instance.
 * @param modifier Modifier for styling and layout customization.
 */
@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    articleId: String,
    onNavigateUp: () -> Unit,
    onPodcastClick: (audioId: String) -> Unit,
    viewModel: ArticleViewModel = hiltViewModel(),
) {
    val articleEntryPoint = EntryPointAccessors.fromApplication(
        LocalContext.current,
        ArticleEntryPoint::class.java
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val analytics = LocalApp.current.analytics
    SideEffect {
        analytics.logEvent(
            analytics.event.screenView, mapOf(
                analytics.param.screenName to "Article",
                analytics.param.screenClass to "ArticleScreen"
            )
        )
    }

    LaunchedEffect(Unit) {
        if (uiState.isLoading && uiState.content.isEmpty()) {
            viewModel.sendIntent(Intent.FetchArticle(articleId))
        }
    }
    ArticleScreen(
        uiState = uiState,
        adUnitId = articleEntryPoint.getArticleAdKey(),
        onNavigateUp = onNavigateUp,
        onPodcastClick = onPodcastClick,
        onTryAgain = {
            analytics.logEvent(
                analytics.event.tryAgain, mapOf(
                    analytics.param.itemId to articleId,
                )
            )
            viewModel.sendIntent(Intent.FetchArticle(articleId))
        },
        modifier = modifier,
    )
}

/**
 * Composable function that displays the main content of an article.
 *
 * This composable orchestrates the display of an article, handling various states like loading, error, and success.
 * It features a collapsing top image that smoothly transitions out of view as the user scrolls down,
 * providing an engaging and interactive reading experience. The article content, including the title, image,
 * and body, is presented within a scrollable layout. It can also display a podcast card if available.
 *
 * The collapsing behavior of the top image is achieved through nested scrolling, allowing for a natural
 * and responsive animation.
 *
 * @param uiState The [UiArticleState] representing the current state of the article, including content, loading, and error status.
 * @param onNavigateUp Callback invoked when the user wants to navigate up (e.g., back).
 * @param onPodcastClick Callback invoked when the user clicks on the podcast card. It provides the podcast's title and audio URL.
 * @param onTryAgain Callback invoked when there's an error and the user wants to retry loading the article.
 * @param modifier Modifier for styling and layout customization.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArticleScreen(
    uiState: UiArticleState,
    adUnitId: String,
    onNavigateUp: () -> Unit,
    onPodcastClick: (audioId: String) -> Unit,
    onTryAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val maxSize = 320.dp
    val safeContent = WindowInsets.Companion.safeContent.asPaddingValues()
    var scrollOffset by rememberSaveable { mutableIntStateOf(0) }
    val contentScrollState = rememberScrollState()
    val shouldComeDown by remember {
        derivedStateOf {
            contentScrollState.value < maxSize.value + 56.dp.value
        }
    }

    val rememberedOnPodcastClick = remember<(UiPodcast) -> Unit> {
        { podcast -> onPodcastClick(podcast.id) }
    }

    Box(
        modifier = modifier
            .nestedScroll(object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val newYTranslation = if (shouldComeDown)
                    // I'm multiplying by 2 to speed up the animation
                        available.y.toInt() * 2 + scrollOffset else scrollOffset

                    // maxSize.value * 2 + safeContent gives the total amount to make sure the
                    // components goes away
                    scrollOffset = newYTranslation.coerceIn(
                        -(maxSize.value * 2 + safeContent.calculateTopPadding().value).toInt(), 0
                    )
                    return super.onPreScroll(available, source)
                }
            })
    ) {
        uiState.error?.let {
            ShowError(
                onTryAgain = onTryAgain, modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
                    .padding(horizontal = 16.dp)
            )
        } ?: if (uiState.isLoading) {
            LoadingArticle(
                Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = safeContent.calculateTopPadding(),
                        bottom = safeContent.calculateBottomPadding()
                    )
            )
        } else {
            BottomTopVerticalGradient(
                Modifier
                    .height(maxSize)
                    .fillMaxWidth()
                    .offset {
                        IntOffset(0, scrollOffset)
                    }
                    .align(Alignment.TopCenter)
            ) {
                AsyncImage(
                    model = uiState.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    imageLoader = LocalImageLoader.current,
                    modifier = Modifier
                        .height(maxSize)
                        .fillMaxWidth()
                        .blur(30.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(bottom = safeContent.calculateBottomPadding())
                    .fillMaxSize()
            ) {
                val windowSize = currentWindowAdaptiveInfo().windowSizeClass
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {}, navigationIcon = {
                        IconButton(onNavigateUp) {
                            Icon(KIcon.ArrowBack, "")
                        }
                    })
                Column(
                    modifier = Modifier
                        .verticalScroll(contentScrollState)
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AsyncImage(
                        model = uiState.imageUrl,
                        contentDescription = null,
                        imageLoader = LocalImageLoader.current,
                        modifier = Modifier
                            .heightIn(
                                max = when (windowSize.windowWidthSizeClass) {
                                    WindowWidthSizeClass.EXPANDED -> 550.dp
                                    else -> 210.dp
                                }
                            )
                            .fillMaxWidth()
                            .wrapContentSize()
                            .align(Alignment.CenterHorizontally)

                    )
                    uiState.podcast?.let {
                        PodcastCard(
                            uiPodcast = it,
                            modifier = Modifier.fillMaxWidth(),
                            onPodcastClick = rememberedOnPodcastClick
                        )
                    }
                    KMarkdown(
                        text = uiState.content,
                    )
                }
                KAdView(
                    adUnitId = adUnitId,
                    screenName = "Article",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ArticleWithPodcastPreview() {
    ArticleExample(
        uiState = UiArticleState(
            isLoading = false,
            title = "This Articles comes with a podcast an a long header to see how it looks",
            content = markdown,
            podcast = UiPodcast(
                title = "Building rock solid apps: The art of testing in jetpack compose",
                id = "",
                audio = ""
            )
        )
    )
}

@PreviewLightDark
@Composable
private fun ArticlePreview() {
    ArticleExample()
}

@PreviewLightDark
@Composable
private fun ArticleErrorPreview() {
    ArticleExample(uiState = UiArticleState(error = ErrorState(ArticleErrors.NetworkError)))
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ArticleExample(
    modifier: Modifier = Modifier,
    uiState: UiArticleState = UiArticleState(
        isLoading = false,
        title = "This text is just a placeholder",
        content = markdown
    ),
) {
    KesiTheme {
        val imageColor = MaterialTheme.colorScheme.tertiaryContainer
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(imageColor.toArgb())
        }
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .build()
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                CompositionLocalProvider(LocalApp provides PreviewAppManager) {
                    ArticleScreen(
                        uiState = uiState,
                        adUnitId = "",
                        onNavigateUp = {},
                        onPodcastClick = { },
                        onTryAgain = {},
                        modifier = modifier,
                    )
                }
            }
        }
    }
}

const val markdown = """
## Unveiling Kesi Android

Kesi Android itself leverages a wide range of modern Android libraries and technologies to bring its features to life. Here's a snapshot of what's under the hood:

- **Hilt:** Used for dependency injection throughout the application, integrating with ViewModels and Navigation Compose.
- **Kotlin Coroutines:** For managing background threads and asynchronous operations efficiently.

```kotlin
val imageColor = MaterialTheme.colorScheme.tertiaryContainer
val previewHandler = AsyncImagePreviewHandler {
    ColorImage(imageColor.toArgb())
}
val imageLoader = ImageLoader.Builder(LocalContext.current)
    .build()
```
"""