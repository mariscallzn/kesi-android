package com.kesicollection.kesiandroid

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kesicollection.articles.articles
import com.kesicollection.core.model.AppRoute
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.doggallery.dogGallery
import com.kesicollection.feature.quiz.QuizRoute
import com.kesicollection.feature.quiz.navigateToQuiz
import com.kesicollection.feature.quiz.quiz
import com.kesicollection.feature.quiztopics.quizTopics

/**
 * [AppNavigation] is the main navigation component for the application.
 * It uses a [NavHost] to define and manage the application's navigation graph.
 *
 * This composable sets up the navigation host and defines the available destinations within it.
 * It handles navigation between different screens, each defined as a composable destination
 * within the [NavHost].
 *
 * @sample com.kesicollection.kesiandroid.ExampleAppNavigation
 *
 * @param navController The [NavHostController] that manages the navigation state and back stack.
 * @param startDestination The initial [AppRoute] where the navigation begins.
 * This should be an instance of a class or object that implements [AppRoute].
 * @param modifier Modifier for styling and layout adjustments applied to the underlying `NavHost`.
 * Defaults to [Modifier] for no specific styling.
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
        quiz(
            modifier = modifier,
            onNavigateUp = navController::onNavigateUp
        )
        quizTopics(
            modifier = modifier,
            onTopicClick = navController::onTopicClick
        )
        dogGallery(modifier = modifier)
        articles(modifier = modifier, onArticleClick = {
            Log.d(this::class.java.name, "AppNavigation: onArticleClick $it")
        })
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

/**
 * Navigates to the Quiz screen when a topic is clicked.
 *
 * This function is an extension function for [NavController]. It takes the topic's ID and name
 * as parameters and uses them to construct a [QuizRoute] object. This object is then passed
 * to the [navigateToQuiz] function to perform the actual navigation.
 *
 * @param id The unique identifier of the clicked topic.
 * @param name The name of the clicked topic, which will be displayed on the Quiz screen.
 *
 * @see QuizRoute
 * @see navigateToQuiz
 */
private fun NavController.onTopicClick(id: String, name: String) {
    navigateToQuiz(QuizRoute(id = id, name = name))
}

@Composable
private fun ExampleAppNavigation(modifier: Modifier) {
    KesiTheme {
        val navController = rememberNavController()
        AppNavigation(
            navController = navController,
            startDestination = QuizRoute(
                id = "",
                name = "Jetpack Compose"
            ),
            modifier = modifier
        )
    }
}