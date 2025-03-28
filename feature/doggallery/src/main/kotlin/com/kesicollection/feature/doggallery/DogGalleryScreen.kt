package com.kesicollection.feature.doggallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dagger.hilt.android.EntryPointAccessors

// https://dog.ceo/dog-api/documentation/breed

val LocalImageLoader = compositionLocalOf<ImageLoader> {
    error("")
}

@Composable
fun DogGalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: DogGalleryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val imageLoader = EntryPointAccessors.fromApplication(
        LocalContext.current.applicationContext,
        ImageLoaderEntryPoint::class.java
    ).imageLoader()

    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        DogGalleryScreen(
            uiState = uiState,
            modifier = modifier,
        )
    }
}

@Composable
fun DogGalleryScreen(
    uiState: UiDoggGalleryState,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://images.dog.ceo/breeds/corgi-cardigan/n02113186_4711.jpg")
            .crossfade(true)
            .build(),
        imageLoader = LocalImageLoader.current,
        contentDescription = ""
    )
}