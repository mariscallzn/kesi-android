package com.kesicollection.feature.doggallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.theme.KesiTheme
import dagger.hilt.android.EntryPointAccessors

val LocalImageLoader = compositionLocalOf<ImageLoader> {
    error("")
}

@Composable
fun DogGalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: DogGalleryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//    val imageLoader = EntryPointAccessors.fromApplication(
//        LocalContext.current.applicationContext,
//        ImageLoaderEntryPoint::class.java
//    ).imageLoader()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(Intent.FetchDogsByBreed("maltese"))
    }

//    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        DogGalleryScreen(
            uiState = uiState,
            modifier = modifier,
        )
//    }
}

@Composable
fun DogGalleryScreen(
    uiState: UiDoggGalleryState,
    modifier: Modifier = Modifier
) {
    KScaffold { innerPadding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(200.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(uiState.images, key = { it }) { image ->
//                AsyncImage(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(5))
//                        .fillMaxWidth()
//                        .wrapContentHeight(),
//                    contentScale = ContentScale.Crop,
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(image)
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = "",
//                    imageLoader = LocalImageLoader.current
//                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun DogGalleryScreenPreview() {
    DogGalleryScreenExample()
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun DogGalleryScreenExample(modifier: Modifier = Modifier) {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.Red.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        CompositionLocalProvider(
            LocalImageLoader provides ImageLoader.Builder(LocalContext.current).build()
        ) {
            KesiTheme {
                DogGalleryScreen(
                    modifier = modifier,
                    uiState = UiDoggGalleryState(
                        images = listOf(
                            "https://images.dog.ceo/breeds/african/n02116738_10064.jpg",
                            "https://images.dog.ceo/breeds/african/n02116738_10065.jpg"
                        )
                    ),
                )
            }
        }
    }
}