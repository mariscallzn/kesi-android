package com.kesicollection.feature.doggallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

// https://dog.ceo/dog-api/documentation/breed

@Composable
fun DogGalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: DogGalleryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DogGalleryScreen(
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
fun DogGalleryScreen(
    uiState: UiDoggGalleryState,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://images.dog.ceo/breeds/maltese/n02085936_9632.jpg")
            .crossfade(true)
            .build(),
        contentDescription = ""
    )
}