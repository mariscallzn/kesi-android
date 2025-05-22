package com.kesicollection.feature.discover

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kesicollection.core.app.AppRoute
import kotlinx.serialization.Serializable

@Serializable
data object Discover : AppRoute

fun NavGraphBuilder.discover(
    modifier: Modifier = Modifier
) {
    composable<Discover> {
        DiscoverScreen(
            modifier = modifier,
            onSeeAllClick = {},
            onContentClick = {}
        )
    }
}