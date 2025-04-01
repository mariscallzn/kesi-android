package com.kesicollection.feature.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.theme.KIcon

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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Text(uiState.tmpText)
        }
    }
}