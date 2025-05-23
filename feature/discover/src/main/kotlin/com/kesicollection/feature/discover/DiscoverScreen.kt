package com.kesicollection.feature.discover

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.model.ContentType
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.component.KScaffold
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

    LaunchedEffect(uiState) {
        if (uiState is UiState.Loading) {
            viewModel.sendIntent(Intent.FetchFeatureItems)
        }
    }

    DiscoverScreen(
        uiState = uiState,
        onSeeAllClick = onSeeAllClick,
        onContentClick = onContentClick,
        modifier = modifier
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverScreen(
    uiState: UiState,
    onSeeAllClick: (UICategory) -> Unit,
    onContentClick: (UIContent) -> Unit,
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
            )

            is UiState.DiscoverContent -> DiscoverContent(
                uiState = uiState,
                onSeeAllClick = onSeeAllClick,
                onContentClick = onContentClick,
                modifier = Modifier
                    .padding(innerPadding)
            )

            is UiState.Error -> {
                // Handle error state, for example by showing a text message
                Text(
                    text = "Error: ${uiState.error}",
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
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
    LazyColumn(
        modifier = modifier,
    ) {
        featuredContentSection(
            featuredContent = uiState.featuredContent,
            onContentClick = onContentClick
        )
        promotedContentSections(
            promotedContent = uiState.promotedContent,
            onSeeAllClick = onSeeAllClick,
            onContentClick = onContentClick
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


@Composable
private fun ExampleDiscoverScreen(
    modifier: Modifier = Modifier,
    uiState: UiState = contentSample // You might want to use contentSample for previewing content
) {
    KesiTheme {
        DiscoverScreen(
            uiState = uiState,
            onSeeAllClick = {},
            onContentClick = {},
            modifier = modifier
        )
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