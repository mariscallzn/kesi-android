package com.kesicollection.kesiandroid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kesicollection.core.model.AppRoute
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiz.QuizRoute
import com.kesicollection.feature.quiz.quiz

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
        quiz(modifier)
    }
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