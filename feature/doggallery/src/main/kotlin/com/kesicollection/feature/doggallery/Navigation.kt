package com.kesicollection.feature.doggallery

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kesicollection.core.model.AppRoute
import kotlinx.serialization.Serializable

@Serializable
data object DogGallery : AppRoute

fun NavGraphBuilder.dogGallery(modifier: Modifier = Modifier) {
    composable<DogGallery> {
        DogGalleryScreen(modifier = modifier)
    }
}