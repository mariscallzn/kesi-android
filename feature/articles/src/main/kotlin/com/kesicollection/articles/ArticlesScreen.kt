package com.kesicollection.articles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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
import com.kesicollection.articles.components.Article
import com.kesicollection.articles.components.LoadingArticles
import com.kesicollection.articles.model.UiArticle
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.component.KAdView
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.component.ShowError
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.articles.R
import com.kesicollection.core.uisystem.R as CoreUiSystemR

/**
 * Composable function that displays a screen for listing articles.
 *
 * This function fetches articles from the [ArticlesViewModel] and displays them in a list.
 * It also handles the initial loading state and provides a mechanism to navigate to a
 * detailed view of an article when clicked.
 *
 * The [ImageLoader] is provided through a [CompositionLocal], ensuring that components
 * within this screen can easily access it for image loading operations.
 *
 * @param modifier Modifier for styling the layout.
 * @param onArticleClick Callback invoked when an article is clicked, providing the article ID.
 * @param viewModel The [ArticlesViewModel] instance used to manage and retrieve article data.
 * It defaults to an instance provided by [hiltViewModel].
 */
@Composable
fun ArticlesScreen(
    modifier: Modifier = Modifier,
    onArticleClick: (articleId: String) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: ArticlesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val analytics = LocalApp.current.analytics

    SideEffect {
        analytics.logEvent(
            analytics.event.screenView, mapOf(
                analytics.param.screenName to "Articles",
                analytics.param.screenClass to "ArticlesScreen"
            )
        )
    }

    LaunchedEffect(Unit) {
        if (uiState.isLoading && uiState.articles.isEmpty()) {
            viewModel.sendIntent(Intent.FetchArticles)
        }
    }

    ArticlesScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        onArticleClick = onArticleClick,
        onBookmarkClick = {
            analytics.logEvent(
                analytics.event.selectItem, mapOf(
                    analytics.param.itemId to it.articleId,
                    analytics.param.contentType to "article"
                )
            )
            viewModel.sendIntent(it)
        },
        onTryAgain = {
            analytics.logEvent(
                analytics.event.tryAgain, mapOf(
                    analytics.param.screenName to "articles",
                )
            )
            viewModel.sendIntent(it)
        },
        uiState = uiState,
        adUnitId = uiState.adKey
    )
}

/**
 * Internal composable function that renders the articles screen.
 *
 * This function displays the list of articles based on the provided [UiArticlesState].
 * It gracefully handles different UI states, such as loading, success, and error.
 *
 * - **Loading State:** When `uiState.isLoading` is true, a loading indicator ([LoadingArticles]) is shown.
 * - **Success State:** When `uiState.isLoading` is false and `uiState.screenError` is null, it renders a list of articles using [LazyColumn] and [Article] composables.
 *   - Each article is made clickable, invoking the [onArticleClick] callback with the clicked article's ID.
 *   - A divider visually separates each article in the list.
 * - **Error State:** When `uiState.screenError` is not null, it displays an error message with a retry option via [ShowError], triggered by the [onTryAgain] callback.
 *
 * @param uiState The current state of the articles UI, encompassing the loading state, error state, and the list of articles.
 * @param onArticleClick Callback function invoked when an article is clicked, providing the ID of the clicked article.
 * @param onTryAgain Callback function invoked when the user attempts to retry fetching articles after an error.
 * @param modifier Modifier for styling and customizing the layout of the articles screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArticlesScreen(
    uiState: UiArticlesState,
    adUnitId: String,
    onArticleClick: (articleId: String) -> Unit,
    onBookmarkClick: (Intent.BookmarkClicked) -> Unit,
    onTryAgain: (Intent.FetchArticles) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    KScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
                title = {
                    Text(
                        stringResource(R.string.feature_articles_topbar_title),
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                navigationIcon = {
                    IconButton(onNavigateUp) {
                        Icon(
                            imageVector = KIcon.ArrowBack,
                            stringResource(
                                CoreUiSystemR.string.core_uisystem_navigate_up
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        uiState.error?.let {
            ShowError(
                { onTryAgain(Intent.FetchArticles) }, modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            )
        } ?: if (uiState.isLoading) {
            LoadingArticles(
                modifier = Modifier
                    .padding(innerPadding)
                    .testTag(":feature:articles:loading")
            )
        } else {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(":feature:articles:articles")
                ) {
                    itemsIndexed(
                        uiState.articles,
                        key = { _, item -> item.articleId }) { index, item ->
                        Article(
                            article = item,
                            onArticleClick = { onArticleClick(it.articleId) },
                            onBookmarkClick = { onBookmarkClick(Intent.BookmarkClicked(it)) },
                        )
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
                KAdView(
                    adUnitId = adUnitId,
                    screenName = "Articles",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ArticlesScreenPreview() {
    ArticlesScreenExample()
}

@PreviewLightDark
@Composable
private fun LoadingArticlesScreenPreview() {
    ArticlesScreenExample(uiState = UiArticlesState(isLoading = true))
}

@PreviewLightDark
@Composable
private fun ErrorArticlesScreenPreview() {
    ArticlesScreenExample(uiState = UiArticlesState(error = ErrorState(ArticlesErrors.NetworkError)))
}

@PreviewLightDark
@Composable
private fun NoErrorNoLoadingAndEmptyPreview() {
    ArticlesScreenExample(
        uiState = UiArticlesState(
            error = null,
            articles = emptyList(),
            isLoading = false
        )
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ArticlesScreenExample(
    modifier: Modifier = Modifier,
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
                ArticlesScreen(
                    modifier = modifier,
                    uiState = uiState,
                    adUnitId = "",
                    onNavigateUp = {},
                    onArticleClick = {},
                    onBookmarkClick = {},
                    onTryAgain = {}
                )
            }
        }
    }
}