package com.kesicollection.feature.discover

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.kesicollection.core.model.ContentType
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.component.ShowError
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.discover.component.LoadingDiscover
import com.kesicollection.feature.discover.component.featuredContentSection
import com.kesicollection.feature.discover.component.promotedContentSections
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    onSeeAllClick: (UICategory) -> Unit,
    onContentClick: (UIContent) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val analytics = LocalApp.current.analytics
    // Log screen view event.
    SideEffect {
        analytics.logEvent(
            analytics.event.screenView, mapOf(
                analytics.param.screenName to "Discover",
                analytics.param.screenClass to "DiscoverScreen"
            )
        )
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Loading) {
            viewModel.sendIntent(Intent.FetchFeatureItems)
        }
    }

    val rememberedOnTryAgain = remember {
        {
            viewModel.sendIntent(Intent.FetchFeatureItems)
            analytics.logEvent(
                analytics.event.tryAgain, mapOf(
                    analytics.param.screenName to "Discover",
                    analytics.param.screenClass to "DiscoverScreen"
                )
            )
        }
    }

    DiscoverScreen(
        uiState = uiState,
        onSeeAllClick = onSeeAllClick,
        onContentClick = onContentClick,
        onTryAgain = rememberedOnTryAgain,
        modifier = modifier
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiscoverScreen(
    uiState: UiState,
    onSeeAllClick: (UICategory) -> Unit,
    onContentClick: (UIContent) -> Unit,
    onTryAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    KScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
                title = {
                    Text("Kesi Android", style = MaterialTheme.typography.titleMedium)
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        when (uiState) {
            UiState.Loading -> LoadingDiscover(
                Modifier
                    .padding(innerPadding)
                    .padding(start = 16.dp, top = 16.dp)
                    .testTag(":feature:discover:LoadingDiscover")
            )

            is UiState.DiscoverContent -> DiscoverContent(
                uiState = uiState,
                onSeeAllClick = onSeeAllClick,
                onContentClick = onContentClick,
                modifier = Modifier
                    .padding(innerPadding)
                    .testTag(":feature:discover:DiscoverContent")
            )

            is UiState.Error -> {
                ShowError(
                    onTryAgain = onTryAgain,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .testTag(":feature:discover:ShowError")
                )
            }
        }
    }
}

@Composable
fun DiscoverContent(
    uiState: UiState.DiscoverContent,
    modifier: Modifier = Modifier,
    onSeeAllClick: (UICategory) -> Unit,
    onContentClick: (UIContent) -> Unit,
) {
    val analytics = LocalApp.current.analytics
    val rememberedOnContentClick = remember<(UIContent, String) -> Unit> {
        { content, emphasis ->
            onContentClick(content)
            analytics.logEvent(
                analytics.event.selectItem, mapOf(
                    analytics.param.itemId to content.id,
                    analytics.param.contentType to content.type.name.lowercase(),
                    analytics.param.contentEmphasis to emphasis
                )
            )
        }
    }
    val rememberedOnSeeAllClick = remember<(UICategory) -> Unit> {
        {
            onSeeAllClick(it)
            analytics.logEvent(
                analytics.event.onSeeAll, mapOf(
                    analytics.param.itemId to it.id,
                    analytics.param.itemName to it.name,
                )
            )
        }
    }
    val onFeatureContentClick: (UIContent) -> Unit = remember {
        { rememberedOnContentClick(it, "featured") }
    }
    val onPromotedContentClick: (UIContent) -> Unit = remember {
        { rememberedOnContentClick(it, "promoted") }
    }

    LazyColumn(
        modifier = modifier,
    ) {
        if (uiState.featuredContent.isNotEmpty()) {
            featuredContentSection(
                featuredContent = uiState.featuredContent,
                onContentClick = onFeatureContentClick
            )
        }
        promotedContentSections(
            promotedContent = uiState.promotedContent,
            onSeeAllClick = rememberedOnSeeAllClick,
            onContentClick = onPromotedContentClick
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewDiscoverScreenLoading() {
    ExampleDiscoverScreen(uiState = UiState.Loading)
}

@PreviewLightDark
@Composable
private fun PreviewDiscoverScreenContent() {
    ExampleDiscoverScreen(uiState = contentSample) // Use the sample data
}

@PreviewLightDark
@Composable
private fun PreviewDiscoverScreenError() {
    ExampleDiscoverScreen(uiState = UiState.Error(ErrorState(DiscoverErrors.GenericError)))
}


@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ExampleDiscoverScreen(
    modifier: Modifier = Modifier,
    uiState: UiState = contentSample // You might want to use contentSample for previewing content
) {
    KesiTheme {
        // Image color for preview.
        val imageColor = MaterialTheme.colorScheme.tertiaryContainer
        // Preview handler for async image.
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(imageColor.toArgb())
        }

        // Image loader for preview.
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .build()

        // Provide local async image preview handler and image loader.
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                DiscoverScreen(
                    uiState = uiState,
                    onSeeAllClick = {},
                    onContentClick = {},
                    onTryAgain = {},
                    modifier = modifier
                )
            }
        }
    }
}

val contentSample = UiState.DiscoverContent(

    featuredContent = persistentListOf(
        UIContent(
            id = "feat1",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Article,
            title = "Featured Article 1",
            description = "Description for featured article 1."
        ),
        UIContent(
            id = "feat2",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Video,
            title = "Featured Video 2",
            description = "Description for featured video 2."
        ),
        UIContent(
            id = "feat3",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Podcast,
            title = "Featured Podcast 3",
            description = "Description for featured podcast 3."
        ),
        UIContent(
            id = "feat4",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Article,
            title = "Featured Article 4",
            description = "Description for featured article 4."
        ),
        UIContent(
            id = "feat5",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Video,
            title = "Featured Video 5",
            description = "Description for featured video 5."
        ),
    ),
    promotedContent = persistentMapOf(
        UICategory(id = "arch", name = "Architecture") to persistentListOf(
            UIContent(
                id = "arch_promo1",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "Arch Promo 1",
                description = "Desc Arch Promo 1"
            ),
            UIContent(
                id = "arch_promo2",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "Arch Promo 2",
                description = "Desc Arch Promo 2"
            ),
            UIContent(
                id = "arch_promo3",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "Arch Promo 3",
                description = "Desc Arch Promo 3"
            ),
            UIContent(
                id = "arch_promo4",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "Arch Promo 4",
                description = "Desc Arch Promo 4"
            ),
            UIContent(
                id = "arch_promo5",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "Arch Promo 5",
                description = "Desc Arch Promo 5"
            ),
        ),
        UICategory(id = "ui", name = "UI") to persistentListOf(
            UIContent(
                id = "ui_promo1",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "UI Promo 1",
                description = "Desc UI Promo 1"
            ),
            UIContent(
                id = "ui_promo2",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "UI Promo 2",
                description = "Desc UI Promo 2"
            ),
            UIContent(
                id = "ui_promo3",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "UI Promo 3",
                description = "Desc UI Promo 3"
            ),
            UIContent(
                id = "ui_promo4",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "UI Promo 4",
                description = "Desc UI Promo 4"
            ),
            UIContent(
                id = "ui_promo5",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "UI Promo 5",
                description = "Desc UI Promo 5"
            ),
        ),
        UICategory(id = "core", name = "Core") to persistentListOf(
            UIContent(
                id = "core_promo1",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "Core Promo 1",
                description = "Desc Core Promo 1"
            ),
            UIContent(
                id = "core_promo2",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "Core Promo 2",
                description = "Desc Core Promo 2"
            ),
            UIContent(
                id = "core_promo3",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "Core Promo 3",
                description = "Desc Core Promo 3"
            ),
            UIContent(
                id = "core_promo4",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "Core Promo 4",
                description = "Desc Core Promo 4"
            ),
            UIContent(
                id = "core_promo5",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "Core Promo 5",
                description = "Desc Core Promo 5"
            ),
        )
    )
)