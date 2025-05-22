package com.kesicollection.articles

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kesicollection.core.app.AppRoute
import kotlinx.serialization.Serializable

@Serializable
data object ArticlesRoute : AppRoute

fun NavGraphBuilder.articles(
    modifier: Modifier = Modifier,
    onArticleClick: (id: String) -> Unit,
) {
    composable<ArticlesRoute> {
        ArticlesScreen(modifier = modifier, onArticleClick)
    }
}