package com.kesicollection.feature.audioplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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
import com.kesicollection.core.model.Podcast
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.component.KCard
import com.kesicollection.core.uisystem.component.KScaffold
import com.kesicollection.core.uisystem.component.ShowError
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.audioplayer.component.LoadingAudioPlayer
import com.kesicollection.feature.audioplayer.player.PlayerStateUi

/**
 * Audio player screen.
 * @param id The id of the podcast to play.
 * @param onNavigateUp Callback to navigate up.
 * @param modifier The modifier to be applied to the component.
 * @param viewModel The view model to use.
 *
 * @see AudioPlayerViewModel
 */
@Composable
fun AudioPlayerScreen(
    id: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var progress by remember { mutableFloatStateOf(0f) }

    val analytics = LocalApp.current.analytics
    // Log screen view event.
    SideEffect {
        analytics.logEvent(
            analytics.event.screenView, mapOf(
                analytics.param.screenName to "AudioPlayer",
                analytics.param.screenClass to "AudioPlayerScreen"
            )
        )
    }

    // Fetch podcast if not loaded.
    LaunchedEffect(Unit) {
        if (uiState.isLoading && uiState.podcast == null) {
            viewModel.sendIntent(Intent.FetchPodcast(id))
        }
    }

    LaunchedEffect(uiState.podcast, uiState.playerState) {
        // Initialize screen when podcast is loaded and player is not idle or ended.
        uiState.podcast?.let {
            if (uiState.playerState != PlayerStateUi.Idle
                || uiState.playerState != PlayerStateUi.Ended
            ) {
                viewModel.sendIntent(Intent.InitScreen(it))
            }
        }
    }

    // Collect current position from view model.
    LaunchedEffect(Unit) {
        viewModel.currentPositionMs.collect {
            progress = it
        }
    }

    // Remember reply 10 click action.
    val rememberedOnReply10Click = remember {
        {
            viewModel.sendIntent(Intent.SeekTo(-10f * 1000))
            analytics.logEvent(
                analytics.event.reply10AudioPlayer, mapOf(
                    analytics.param.itemId to (uiState.podcast?.audio ?: ""),
                    analytics.param.contentType to "podcast"
                )
            )
        }
    }

    // Remember forward 10 click action.
    val rememberedOnForward10Click = remember {
        {
            viewModel.sendIntent(Intent.SeekTo(10f * 1000))
            analytics.logEvent(
                analytics.event.forward10AudioPlayer, mapOf(
                    analytics.param.itemId to (uiState.podcast?.audio ?: ""),
                    analytics.param.contentType to "podcast"
                )
            )
        }
    }

    // Remember play/pause click action.
    val rememberedOnPlayPauseClick = remember {
        {
            // Log event based on player state.
            if (uiState.playerState == PlayerStateUi.Playing) {
                analytics.logEvent(
                    analytics.event.pauseAudioPlayer, mapOf(
                        analytics.param.itemId to (uiState.podcast?.audio ?: ""),
                        analytics.param.contentType to "podcast"
                    )
                )
                viewModel.sendIntent(Intent.PauseAudio)
            } else {
                viewModel.sendIntent(Intent.PlayAudio)
                analytics.logEvent(
                    analytics.event.playAudioPlayer, mapOf(
                        analytics.param.itemId to (uiState.podcast?.audio ?: ""),
                        analytics.param.contentType to "podcast"
                    )
                )
            }
        }
    }

    // Remember try again action.
    val rememberedOnTryAgain = remember {
        {
            // Fetch podcast and log event.
            viewModel.sendIntent(Intent.FetchPodcast(id))
            analytics.logEvent(
                analytics.event.tryAgain, mapOf(
                    analytics.param.itemId to (uiState.podcast?.audio ?: ""),
                    analytics.param.contentType to "podcast"
                )
            )
        }
    }

    // Render audio player screen.
    AudioPlayerScreen(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        modifier = modifier,
        progress = { progress },
        isPlaying = { uiState.playerState == PlayerStateUi.Playing },
        onReply10Click = rememberedOnReply10Click,
        onForward10Click = rememberedOnForward10Click,
        onPlayPauseClick = rememberedOnPlayPauseClick,
        onTryAgain = rememberedOnTryAgain
    )
}

/**
 * Audio player screen.
 * @param modifier The modifier to be applied to the component.
 * @param uiState The state of the screen.
 * @param onNavigateUp Callback to navigate up.
 * @param progress The progress of the audio player.
 * @param isPlaying Whether the audio player is playing.
 * @param onPlayPauseClick Callback when the play/pause button is clicked.
 * @param onReply10Click Callback when the reply 10 seconds button is clicked.
 * @param onForward10Click Callback when the forward 10 seconds button is clicked.
 * @param onTryAgain Callback when the try again button is clicked.
 * @see UiAudioPlayerState
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AudioPlayerScreen(
    modifier: Modifier = Modifier,
    uiState: UiAudioPlayerState,
    onNavigateUp: () -> Unit,
    progress: () -> Float,
    isPlaying: () -> Boolean,
    onPlayPauseClick: () -> Unit,
    onReply10Click: () -> Unit,
    onForward10Click: () -> Unit,
    onTryAgain: () -> Unit,
    // Start of the audio player screen content.
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            uiState.podcast?.let {
                // Background image with blur effect.
                AsyncImage(
                    model = it.img,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .blur(radius = 16.dp),
                    imageLoader = LocalImageLoader.current,
                    contentDescription = null
                )
            }
            // Background overlay with transparency.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
            )
        }

        // Scaffold with transparent container color and top app bar.
        KScaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {},
                    navigationIcon = {
                        IconButton(onNavigateUp) {
                            Icon(
                                KIcon.ArrowBack,
                                "",
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            when {
                // Show error message if error is not null.
                uiState.error != null -> ShowError(
                    onTryAgain = onTryAgain, modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )

                // Show loading indicator if loading and podcast is null.
                uiState.isLoading && uiState.podcast == null -> LoadingAudioPlayer(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )

                // Show audio player content.
                else -> AudioPlayerContent(
                    uiState = uiState,
                    progress = progress,
                    isPlaying = isPlaying,
                    onPlayPauseClick = onPlayPauseClick,
                    onReply10Click = onReply10Click,
                    onForward10Click = onForward10Click,
                    modifier = modifier.padding(innerPadding),
                )
            }
        }
    }
}

/**
 * Audio player content.
 * @param uiState The state of the screen.
 * @param progress The progress of the audio player.
 * @param isPlaying Whether the audio player is playing.
 * @param onPlayPauseClick Callback when the play/pause button is clicked.
 * @param onReply10Click Callback when the reply 10 seconds button is clicked.
 * @param onForward10Click Callback when the forward 10 seconds button is clicked.
 * @param modifier The modifier to be applied to the component.
 * @see UiAudioPlayerState
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerContent(
    uiState: UiAudioPlayerState,
    progress: () -> Float,
    isPlaying: () -> Boolean,
    onPlayPauseClick: () -> Unit,
    onReply10Click: () -> Unit,
    onForward10Click: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Modifier for reply 10 button.
    val replay10Modifier = remember {
        Modifier
            .clip(CircleShape)
            .size(56.dp)
            .clickable(onClick = onReply10Click)
    }

    // Modifier for forward 10 button.
    val forward10Modifier = remember {
        Modifier
            .clip(CircleShape)
            .size(56.dp)
            .clickable(onClick = onForward10Click)
    }

    // Column layout for audio player content.
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            KCard(
                // Card for podcast image.
                modifier = Modifier
            ) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                ) {
                    // Podcast image or default icon.
                    uiState.podcast?.let {
                        AsyncImage(
                            model = it.img,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .matchParentSize(),
                            imageLoader = LocalImageLoader.current,
                            contentDescription = null
                        )
                    } ?: Icon(
                        KIcon.Podcasts, null, modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            // Podcast title.
            Text(
                uiState.podcast?.title ?: "",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Progress indicator.
            LinearProgressIndicator(progress = progress)
        }
        Spacer(Modifier.height(32.dp))
        // Row layout for player controls.
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reply 10 button.
            Icon(
                KIcon.Replay10,
                null,
                modifier = replay10Modifier,
                tint = MaterialTheme.colorScheme.primary
            )
            // Play/pause button.
            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .size(76.dp)
                    .clickable(onClick = onPlayPauseClick)
            ) {
                // Play/pause icon based on player state.
                Icon(
                    if (isPlaying()) KIcon.Pause else KIcon.PlayArrow,
                    null,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Forward 10 button.
            Icon(
                KIcon.Forward10,
                null,
                modifier = forward10Modifier,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Audio player preview.
 */
@PreviewLightDark
@Composable
private fun AudioPlayerPreview() {
    AudioPlayerExample(
        modifier = Modifier.fillMaxSize(),
        uiAudioPlayerState = UiAudioPlayerState(
            isLoading = false,
            podcast = Podcast(
                id = "1",
                title = "Exploring the Cosmos: A Journey Through Space and Time",
                audio = "https://example.com/podcast.mp3",
                img = "https://example.com/podcast_image.jpg"
            )
        )
    )
}

/**
 * Audio player example.
 * @param uiAudioPlayerState The state of the screen.
 * @param modifier The modifier to be applied to the component.
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
private fun AudioPlayerExample(
    uiAudioPlayerState: UiAudioPlayerState,
    modifier: Modifier = Modifier
) {
    KesiTheme {
        // Image color for preview.
        val imageColor = MaterialTheme.colorScheme.tertiaryContainer
        // Preview handler for async image.
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(imageColor.toArgb())
        }

        // Image loader for preview.
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .build()

        // Provide local async image preview handler and image loader.
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                AudioPlayerScreen(
                    uiState = uiAudioPlayerState,
                    modifier = modifier,
                    onNavigateUp = {},
                    progress = { 0f },
                    isPlaying = { true },
                    onPlayPauseClick = {},
                    onForward10Click = {},
                    onReply10Click = {},
                    onTryAgain = {}
                )
            }
        }
    }
}