package com.kesicollection.feature.audioplayer

import android.content.ComponentName
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.component.KCard
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//TODO: Move it to an inf or shared module.
fun <T> propertyChangeFlow(propertyProvider: () -> T): Flow<T> = flow {
    var previousValue: T = propertyProvider() // Store the initial value
    emit(previousValue) // Emit the initial value

    while (true) {
        delay(100) // Check every 100 milliseconds
        val currentValue = propertyProvider()
        if (currentValue != previousValue) {
            emit(currentValue) // Emit only if the value has changed
            previousValue = currentValue
        }
    }
}

@Composable
fun AudioPlayerScreen(
    title: String,
    fileName: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var mediaController: MediaController? by remember { mutableStateOf(null) }
    var progress by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }

    val analytics = LocalApp.current.analytics
    SideEffect {
        analytics.logEvent(
            analytics.event.screenView, mapOf(
                analytics.param.screenName to "AudioPlayer",
                analytics.param.screenClass to "AudioPlayerScreen"
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(Intent.InitScreen(title))
    }

    LaunchedEffect(Unit) {
        val sessionToken = SessionToken(
            context, ComponentName(context, AudioPlayerService::class.java)
        )
        val controllerFuture = MediaController.Builder(
            context, sessionToken
        ).buildAsync()

        controllerFuture.addListener(
            { mediaController = controllerFuture.get() },
            MoreExecutors.directExecutor()
        )
    }

    LaunchedEffect(mediaController) {
        mediaController?.let {
            it.setMediaItem(MediaItem.fromUri("https://raw.githubusercontent.com/kesicollection/kesi-android-api-data/refs/heads/v1/podcasts/$fileName"))
            it.prepare()
            it.play()
        }
    }

    LaunchedEffect(Unit) {
        propertyChangeFlow { mediaController?.currentPosition?.toFloat() ?: 1f }
            .collect { newValue ->
                val duration = mediaController?.let {
                    if (it.duration < 0) 1f else it.duration
                }?.toFloat() ?: 1f
                progress = newValue / duration
            }
    }

    LaunchedEffect(Unit) {
        propertyChangeFlow { mediaController?.isPlaying ?: false }
            .collect { newValue ->
                isPlaying = newValue
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaController?.release()
        }
    }

    AudioPlayerScreen(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        modifier = modifier,
        progress = {
            progress
        },
        isPlaying = { isPlaying },
        onPlayPauseClick = {
            if (isPlaying) {
                mediaController?.pause()
            } else {
                mediaController?.play()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AudioPlayerScreen(
    modifier: Modifier = Modifier,
    uiState: UiAudioPlayerState,
    onNavigateUp: () -> Unit,
    progress: () -> Float,
    isPlaying: () -> Boolean,
    onPlayPauseClick: () -> Unit,
) {
    val safeContent = WindowInsets.Companion.safeContent.asPaddingValues()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            title = {}, navigationIcon = {
                IconButton(onNavigateUp) {
                    Icon(KIcon.ArrowBack, "")
                }
            }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            KCard {
                Box(modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)) {
                    Text("\uD83D\uDEA7 This screen is under construction \uD83D\uDEA7")
                }
            }

            KCard(
                modifier = Modifier
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(250.dp)
                ) {
                    Icon(
                        KIcon.Podcasts, null, modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            Text(
                uiState.title,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            LinearProgressIndicator(progress = progress)
        }
        Box(
            modifier = Modifier
                .padding(bottom = safeContent.calculateBottomPadding() + 16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground)
                .size(76.dp)
        ) {
            Icon(
                if (isPlaying()) KIcon.Pause else KIcon.PlayArrow,
                null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
                    .clickable { onPlayPauseClick() },
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AudioPlayerPreview() {
    AudioPlayerExample(
        modifier = Modifier.fillMaxSize(),
        uiAudioPlayerState = UiAudioPlayerState(
            title = "This is the title of the podcast, cool ah?",
            isPlaying = true
        )
    )
}

@Composable
private fun AudioPlayerExample(
    uiAudioPlayerState: UiAudioPlayerState,
    modifier: Modifier = Modifier
) {
    KesiTheme {
        AudioPlayerScreen(
            uiState = uiAudioPlayerState,
            modifier = modifier,
            onNavigateUp = {},
            progress = { 0f },
            isPlaying = { true },
            onPlayPauseClick = {},
        )
    }
}