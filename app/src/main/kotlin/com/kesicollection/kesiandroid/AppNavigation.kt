package com.kesicollection.kesiandroid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kesicollection.articles.ArticlesRoute
import com.kesicollection.articles.articles
import com.kesicollection.core.model.AppRoute
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.article.ArticleRoute
import com.kesicollection.feature.article.article
import com.kesicollection.feature.article.navigateToArticle

/**
 * Defines the main navigation structure of the application.
 *
 * This composable sets up the navigation host using [NavHost] and
 * defines the available destinations. It uses a [NavHostController] to
 * manage the navigation state and transitions between different parts of
 * the app.
 *
 * @sample com.kesicollection.kesiandroid.ExampleAppNavigation
 *
 * @param navController The [NavHostController] responsible for managing
 *                      the navigation graph and state.
 * @param startDestination The [AppRoute] representing the initial
 *                         destination to navigate to when the app starts.
 * @param modifier Modifier to be applied to the navigation host.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: AppRoute,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        articles(modifier = modifier, onArticleClick = {
            navController.navigateToArticle(ArticleRoute(it))
        })
        article(
            modifier = modifier,
            onNavigateUp = navController::onNavigateUp
        )
    }
}

/**
 * Navigates up in the navigation hierarchy.
 *
 * This function provides a concise way to perform a "navigate up" action
 * using the `NavController`. It essentially calls [NavController.popBackStack]
 * which attempts to pop the current destination from the back stack and
 * navigate to the previous destination.
 *
 * If there's no previous destination on the back stack, this will effectively do nothing.
 *
 * @receiver [NavController] The navigation controller that manages the navigation graph.
 */
private fun NavController.onNavigateUp() {
    popBackStack()
}

@Composable
private fun ExampleAppNavigation(modifier: Modifier) {
    KesiTheme {
        val navController = rememberNavController()
        AppNavigation(
            navController = navController,
            startDestination = ArticlesRoute,
            modifier = modifier
        )
    }
}