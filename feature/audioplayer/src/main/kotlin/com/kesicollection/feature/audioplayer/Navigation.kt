package com.kesicollection.feature.audioplayer

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.app.AppRoute
import kotlinx.serialization.Serializable

@Serializable
data class AudioPlayerRoute(
    val id: String,
) : AppRoute

fun NavGraphBuilder.audioPlayer(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable<AudioPlayerRoute> { backstackEntry ->
        val data = backstackEntry.toRoute<AudioPlayerRoute>()
        AudioPlayerScreen(
            id = data.id,
            onNavigateUp = onNavigateUp,
            modifier = modifier
        )
    }
}

fun NavController.navigateToAudiPlayer(route: AudioPlayerRoute, options: NavOptions? = null) =
    navigate(route, options)
