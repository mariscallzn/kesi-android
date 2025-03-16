package com.kesicollection.feature.quiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kesicollection.core.model.AppRoute
import kotlinx.serialization.Serializable

/**
 * Represents a route to a specific quiz based on a topic.
 *
 * This data class encapsulates the necessary information to navigate to a quiz screen
 * for a particular topic. It conforms to the [AppRoute] interface, signifying its role
 * as a screen destination within the application's navigation system.
 *
 * @property topic The topic of the quiz. This string uniquely identifies the quiz
 *                  content and is used to fetch relevant quiz data.
 */
@Serializable
data class QuizRoute(val topic: String) : AppRoute

/**
 * Navigates to [QuizScreen].
 *
 * This extension function simplifies navigation to [QuizScreen].
 * It leverages the [NavController] to perform the navigation based on a provided [QuizRoute].
 *
 * @sample com.kesicollection.feature.quiz.ExampleNavigateToQuiz
 *
 * @param route The data class [QuizRoute] used to let know the [NavGraphBuilder] which composable to load
 * @param navOptions Optional navigation options. You can customize the navigation behavior with this, like popUpTo or launchSingleTop.
 *                   Defaults to `null`, meaning default navigation behavior.
 *
 * @see NavController
 * @see NavOptions
 * @see NavGraphBuilder
 */
fun NavController.navigateToQuiz(route: QuizRoute, navOptions: NavOptions? = null) =
    navigate(route = route, navOptions = navOptions)

/**
 * Adds a composable destination to the navigation graph for the quiz screen.
 *
 * This function defines a route to navigate to the [QuizScreen]. It uses
 * Hilt's [hiltViewModel] to provide a ViewModel instance to the screen.
 *
 * @sample com.kesicollection.feature.quiz.ExampleQuizNavGraph
 *
 * @param modifier The modifier to be applied to the `QuizScreen` composable.
 * @throws IllegalArgumentException if the `QuizRoute` cannot be extracted from the back stack entry.
 *
 * @see QuizRoute
 * @see QuizScreen
 * @see hiltViewModel
 * @see composable
 * @see NavGraphBuilder
 */
fun NavGraphBuilder.quiz(
    onNavigateUp: () -> Unit,
    modifier: Modifier
) {
    composable<QuizRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<QuizRoute>()
        QuizScreen(
            viewModel = hiltViewModel(),
            onNavigateUp = onNavigateUp,
            topic = route.topic,
            modifier = modifier
        )
    }
}

@Composable
private fun ExampleQuizNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = QuizRoute("Jetpack compose")
    ) {
        quiz(modifier = modifier, onNavigateUp = {})
    }
}

@Composable
private fun ExampleNavigateToQuiz() {
    val navController = rememberNavController()
    navController.navigateToQuiz(route = QuizRoute("Jetpack Compose"), navOptions = null)
}