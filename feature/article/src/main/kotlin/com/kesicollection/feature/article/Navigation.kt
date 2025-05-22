package com.kesicollection.feature.article

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.app.AppRoute
import kotlinx.serialization.Serializable

/**
 * Represents a route to a specific article within the application.
 *
 * @property articleId The unique identifier of the article.
 */
@Serializable
data class ArticleRoute(val articleId: String) : AppRoute

/**
 * Defines the navigation route for the Article screen.
 *
 * This function adds a composable destination to the navigation graph for the [ArticleScreen].
 * It handles the navigation to the Article screen using the [ArticleRoute].
 *
 * @param modifier The modifier to be applied to the [ArticleScreen].
 */
fun NavGraphBuilder.article(
    onNavigateUp: () -> Unit,
    onPodcastClick: (articleTitle: String, audioUrl: String) -> Unit,
    modifier: Modifier = Modifier
) {
    composable<ArticleRoute> { backstackEntry ->
        val route = backstackEntry.toRoute<ArticleRoute>()
        ArticleScreen(
            articleId = route.articleId,
            onNavigateUp = onNavigateUp,
            onPodcastClick = onPodcastClick,
            modifier = modifier,
        )
    }
}

/**
 * Navigates to a specific article route.
 *
 * @param route The [ArticleRoute] to navigate to.
 * @param options Optional [NavOptions] to configure the navigation.
 */
fun NavController.navigateToArticle(route: ArticleRoute, options: NavOptions? = null) =
    navigate(route, options)