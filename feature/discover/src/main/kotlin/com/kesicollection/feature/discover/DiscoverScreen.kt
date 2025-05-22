package com.kesicollection.feature.discover

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.discover.component.LoadingDiscover
import com.kesicollection.feature.discover.component.featuredContentSection
import com.kesicollection.feature.discover.component.promotedContentSections


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
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = MaterialTheme.colorScheme.background),
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