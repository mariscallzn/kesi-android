package com.kesicollection.articles

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kesicollection.core.app.AppRoute
import kotlinx.serialization.Serializable

/**
 * Represents the route for the articles screen.
 * This object is serializable and extends [AppRoute].
 */
@Serializable
data object ArticlesRoute : AppRoute

/**
 * Navigates to the articles screen.
 *
 * This extension function on [NavController] simplifies the navigation process
 * to the [ArticlesRoute].
 *
 * @param route The [ArticlesRoute] to navigate to. Defaults to `ArticlesRoute`.
 * @param navOptions Optional [NavOptions] to configure the navigation.
 */
fun NavController.navigateToArticles(
    route: ArticlesRoute = ArticlesRoute, navOptions: NavOptions? = null
) = navigate(route, navOptions)

/**
 * Defines the articles screen within the navigation graph.
 *
 * This extension function on [NavGraphBuilder] sets up the composable destination
 * for the [ArticlesRoute].
 *
 * @param modifier Optional [Modifier] to be applied to the [ArticlesScreen].
 * @param onArticleClick A lambda function to be invoked when an article is clicked.
 *                       It takes the article ID as a [String] parameter.
 * @param onNavigateUp A lambda function to be invoked for navigating up from the screen.
 */
fun NavGraphBuilder.articles(
    modifier: Modifier = Modifier,
    onArticleClick: (id: String) -> Unit,
    onNavigateUp: () -> Unit,
) {
    // Defines the composable for the ArticlesRoute.
    composable<ArticlesRoute> {
        ArticlesScreen(
            modifier = modifier,
            onArticleClick = onArticleClick,
            onNavigateUp = onNavigateUp,
        )
    }
}