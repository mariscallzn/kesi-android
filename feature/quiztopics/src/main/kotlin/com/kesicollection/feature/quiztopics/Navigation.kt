package com.kesicollection.feature.quiztopics

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kesicollection.core.uisystem.AppRoute
import com.kesicollection.core.model.Topic
import kotlinx.serialization.Serializable

/**
 * Represents the route for the quiz topics screen.
 *
 * This data object signifies a specific destination within the application's navigation hierarchy,
 * specifically the screen where users can view and select available quiz topics.
 *
 * It implements the [AppRoute] interface, making it compatible with the application's routing system.
 * The [Serializable] annotation enables the object to be serialized and deserialized, which can be
 * useful for storing or transmitting route information.
 */
@Serializable
data object QuizTopicsRoute : AppRoute

/**
 * Extension function for [NavController] to navigate to a specific [QuizTopicsRoute].
 *
 * This function simplifies navigation to different routes within the application that are
 * represented by the [QuizTopicsRoute] sealed class. It allows you to specify navigation
 * options for more complex navigation scenarios.
 *
 * @param route The [QuizTopicsRoute] representing the destination route.
 * @param options Optional [NavOptions] to configure the navigation behavior (e.g., launch
 *                modes, pop up to behavior). Defaults to null, which means default navigation
 *                behavior will be applied.
 */
fun NavController.navigateTo(route: QuizTopicsRoute, options: NavOptions? = null) =
    navigate(route = route, options)

/**
 * Adds a composable destination to the navigation graph for the quiz topics screen.
 *
 * This function defines the route for navigating to the `QuizTopicsScreen`. It sets up the screen
 * within the navigation graph, providing the necessary dependencies and handling the click action
 * on a topic.
 *
 * @param onTopicClick Callback function to be invoked when a topic is clicked. It receives the
 *   [Topic] representing the clicked topic as a parameter.
 * @param modifier Modifier to be applied to the `QuizTopicsScreen`. Defaults to [Modifier].
 */
fun NavGraphBuilder.quizTopics(
    onTopicClick: OnTopicClick,
    modifier: Modifier = Modifier
) {
    composable<QuizTopicsRoute> {
        QuizTopicsScreen(
            viewModel = hiltViewModel(),
            onTopicClick = onTopicClick,
            modifier = modifier,
        )
    }
}