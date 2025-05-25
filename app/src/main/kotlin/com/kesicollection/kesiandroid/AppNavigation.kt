package com.kesicollection.kesiandroid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kesicollection.articles.ArticlesRoute
import com.kesicollection.articles.articles
import com.kesicollection.articles.navigateToArticles
import com.kesicollection.core.app.AppRoute
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.article.ArticleRoute
import com.kesicollection.feature.article.article
import com.kesicollection.feature.article.navigateToArticle
import com.kesicollection.feature.audioplayer.AudioPlayerRoute
import com.kesicollection.feature.audioplayer.audioPlayer
import com.kesicollection.feature.audioplayer.navigateToAudiPlayer
import com.kesicollection.feature.discover.discover

/**
 * Defines the main navigation structure of the Kesi application.
 *
 * This composable sets up the [NavHost] and defines the available destinations.
 * It utilizes a [NavHostController] to manage navigation state and transitions.
 *
 * @sample com.kesicollection.kesiandroid.ExampleAppNavigation
 * @param navController The [NavHostController] managing the navigation graph and state.
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
        discover(
            modifier = modifier,
            onArticleClick = navController::onArticleClick,
            onSeeAllClick = navController::onSeeAllClick,
            onPodcastClick = navController::onPodcastClick
        )
        articles(
            modifier = modifier,
            onArticleClick = navController::onArticleClick,
            onNavigateUp = navController::onNavigateUp,
        )
        article(
            modifier = modifier,
            onNavigateUp = navController::onNavigateUp,
            onPodcastClick = navController::onPodcastClick
        )
        audioPlayer(
            modifier = modifier,
            onNavigateUp = navController::onNavigateUp
        )
    }
}

/**
 * Navigates up in the navigation stack.
 *
 * This extension function calls [NavController.popBackStack] to remove the
 * current destination from the back stack and navigate to the previous one.
 *
 * If there's no previous destination on the back stack, this will effectively do nothing.
 *
 * @receiver [NavController] The navigation controller that manages the navigation graph.
 */
private fun NavController.onNavigateUp() {
    popBackStack()
}

/**
 * Navigates to the article detail screen for the given article ID.
 *
 * This extension function uses [navigateToArticle] with an [ArticleRoute]
 * to transition to the specific article's content.
 *
 * @receiver [NavController] The navigation controller that manages the navigation graph.
 * @param articleId The unique identifier of the article to display.
 */
private fun NavController.onArticleClick(articleId: String) {
    navigateToArticle(ArticleRoute(articleId))
}

/**
 * Navigates to the "See All" articles screen.
 *
 * This extension function utilizes [navigateToArticles] to display a comprehensive
 * list of available articles.
 *
 * @receiver [NavController] The navigation controller that manages the navigation graph.
 */
private fun NavController.onSeeAllClick() {
    navigateToArticles()
}

/**
 * Navigates to the audio player screen for the given podcast ID.
 *
 * This extension function uses [navigateToAudiPlayer] with an [AudioPlayerRoute]
 * to transition to the specific podcast's audio player.
 *
 * @receiver [NavController] The navigation controller that manages the navigation graph.
 */
private fun NavController.onPodcastClick(id: String) {
    navigateToAudiPlayer(AudioPlayerRoute(id))
}

/** Example composable demonstrating the usage of [AppNavigation]. */
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