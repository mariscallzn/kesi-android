package com.kesicollection.kesiandroid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kesicollection.core.model.AppRoute
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiz.QuizRoute
import com.kesicollection.feature.quiz.navigateToQuiz
import com.kesicollection.feature.quiz.quiz
import com.kesicollection.feature.quiztopics.quizTopics

/**
 * [AppNavigation] is the main navigation component for the application.
 * It uses a [NavHost] to manage the navigation between different screens.
 *
 * @sample com.kesicollection.kesiandroid.ExampleAppNavigation
 *
 * @param navController The [NavHostController] that manages the navigation state.
 * @param startDestination The initial destination route where the navigation starts.
 * This must be any data class or data object that inherits from [AppRoute]
 * @param modifier Modifier for styling and layout adjustments of the `NavHost`. Defaults to `Modifier`.
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

//TODO: It should send the whole Topic object.
private fun NavController.onTopicClick(topicId: String) {
    navigateToQuiz(QuizRoute(topicId))
}

@Composable
private fun ExampleAppNavigation(modifier: Modifier) {
    KesiTheme {
        val navController = rememberNavController()
        AppNavigation(
            navController = navController,
            startDestination = QuizRoute(
                topic = "Jetpack Compose"
            ),
            modifier = modifier
        )
    }
}