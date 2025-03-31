package com.kesicollection.articles

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kesicollection.core.model.AppRoute
import kotlinx.serialization.Serializable

@Serializable
data object ArticlesRoute : AppRoute

fun NavGraphBuilder.articles(modifier: Modifier = Modifier) {
    composable<ArticlesRoute> {
        ArticlesScreen(modifier = modifier)
    }
}