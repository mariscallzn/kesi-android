package com.kesicollection.feature.article

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
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
import androidx.compose.ui.text.font.FontWeight
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
import com.kesicollection.core.uisystem.component.BottomTopVerticalGradient
import com.kesicollection.core.uisystem.component.DisplayContent
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.article.components.BulletList
import com.kesicollection.feature.article.components.Code
import com.kesicollection.feature.article.components.LoadingArticle
import com.kesicollection.feature.article.components.Paragraph
import com.kesicollection.feature.article.components.PodcastCard
import com.kesicollection.feature.article.components.SubHeader
import com.kesicollection.feature.article.uimodel.UiPodcast
import dagger.hilt.android.EntryPointAccessors

/**
 * Provides a [CompositionLocal] to access the [ImageLoader] instance.
 * This allows components further down the tree to access the same [ImageLoader]
 * instance without having to pass it through multiple levels of the hierarchy.
 *
 * By default, it throws an error if no [ImageLoader] has been provided.
 * You should use [CompositionLocalProvider] to provide an instance of [ImageLoader].
 */
val LocalImageLoader = compositionLocalOf<ImageLoader> {
    error("")
}

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
    articleId: String,
    onNavigateUp: () -> Unit,
    onPodcastClick: (articleTitle: String, audioUrl: String) -> Unit,
    viewModel: ArticleViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val imageLoader = EntryPointAccessors.fromApplication(
        LocalContext.current,
        ArticleImageLoader::class.java
    ).getImageLoader()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(Intent.FetchArticle(articleId))
    }

    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        ArticleScreen(
            uiState = uiState,
            onNavigateUp = onNavigateUp,
            onPodcastClick = onPodcastClick,
            modifier = modifier,
        )
    }
}

/**
 * Composable function that displays the main content of an article.
 *
 * This composable displays the article's title, image, and content,
 * allowing for a smooth scrolling experience. It also includes a
 * collapsing top image that hides as the user scrolls down.
 *
 * @param uiState The [UiArticleState] representing the current state of the article.
 * @param onNavigateUp Callback invoked when the user wants to navigate up (e.g., back).
 * @param modifier Modifier for styling and layout customization.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArticleScreen(
    uiState: UiArticleState,
    onNavigateUp: () -> Unit,
    onPodcastClick: (articleTitle: String, audioUrl: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("OKOKO", "https://raw.githubusercontent.com/kesicollection/kesi-android-api-data/refs/heads/v1/images/${uiState.imageUrl}")
    val maxSize = 320.dp
    val safeContent = WindowInsets.Companion.safeContent.asPaddingValues()
    var scrollOffset by rememberSaveable { mutableIntStateOf(0) }
    val lazyColumState = rememberLazyListState()
    val shouldComeDown by remember {
        derivedStateOf {
            lazyColumState.firstVisibleItemIndex <= 1
        }
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
        if (uiState.isLoading) {
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
                    model = "https://raw.githubusercontent.com/kesicollection/kesi-android-api-data/refs/heads/v1/images/${uiState.imageUrl}",
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
                LazyColumn(
                    state = lazyColumState,
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = safeContent.calculateBottomPadding()
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item(key = "${uiState.imageUrl}+", contentType = "Image") {
                        AsyncImage(
                            model = "https://raw.githubusercontent.com/kesicollection/kesi-android-api-data/refs/heads/v1/images/${uiState.imageUrl}",
                            contentDescription = null,
                            imageLoader = LocalImageLoader.current,
                            modifier = Modifier
                                .heightIn(
                                    max = when (windowSize.windowWidthSizeClass) {
                                        WindowWidthSizeClass.EXPANDED -> 550.dp
                                        else -> 210.dp
                                    }
                                )
                                .fillParentMaxWidth()
                                .wrapContentSize()
                                .align(Alignment.CenterHorizontally)

                        )
                    }
                    item(key = uiState.title, contentType = "Title") {
                        Text(
                            uiState.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Medium,
                            ),
                            modifier = Modifier.fillParentMaxWidth()
                        )
                    }
                    uiState.podcast?.let { podcast ->
                        stickyHeader {
                            Column(
                                Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                AnimatedVisibility(shouldComeDown) {
                                    Spacer(Modifier.height(16.dp))
                                }
                                PodcastCard(
                                    uiPodcast = podcast,
                                    modifier = Modifier.fillParentMaxWidth(),
                                    onPodcastClick = { onPodcastClick(it.title, it.audioUrl) }
                                )
                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    }
                    items(
                        items = uiState.content,
                        key = { it.uiId },
                        contentType = { it.type }
                    ) {
                        DisplayContent(it, modifier = Modifier.fillParentMaxWidth())
                    }
                }
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
            content = testData,
            podcast = UiPodcast(
                title = "Building rock solid apps: The art of testing in jetpack compose",
                id = "",
                audioUrl = ""
            )
        )
    )
}

@PreviewLightDark
@Composable
private fun ArticlePreview() {
    ArticleExample()
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ArticleExample(
    uiState: UiArticleState = UiArticleState(
        isLoading = false,
        title = "This text is just a placeholder",
        content = testData
    ),
    modifier: Modifier = Modifier
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
                ArticleScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onPodcastClick = { _, _ -> },
                    modifier = modifier,
                )
            }
        }
    }
}

private const val lorem =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

val testData = listOf(
    Paragraph(lorem.take(100)),
    SubHeader("Subheader example with more data to test"),
    BulletList(
        "Bullet list example with more data to test",
        listOf(lorem.take(150))
    ),
    Code(
        """
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
        """.trimIndent()
    ),
    Paragraph(lorem),
)