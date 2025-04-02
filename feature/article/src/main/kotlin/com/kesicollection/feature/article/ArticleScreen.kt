package com.kesicollection.feature.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.uisystem.component.DisplayContent
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.article.intent.testData

@Composable
fun ArticleScreen(
    articleId: String,
    onNavigateUp: () -> Unit,
    viewModel: ArticleViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(Intent.FetchArticle(articleId))
    }

    ArticleScreen(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        modifier = modifier,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    uiState: UiArticleState,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    KScaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = {}, navigationIcon = {
                IconButton(onNavigateUp) {
                    Icon(KIcon.ArrowBack, "")
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(uiState.title)
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.content) {
                    DisplayContent(it)
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ArticlePreview() {
    ArticleExample()
}


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
        ArticleScreen(
            uiState = uiState,
            onNavigateUp = {},
            modifier = modifier,
        )
    }
}