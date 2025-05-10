package com.kesicollection.feature.audioplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.component.KCard
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme

@Composable
fun AudioPlayerScreen(
    title: String,
    fileName: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var progress by remember { mutableFloatStateOf(0f) }

    val analytics = LocalApp.current.analytics
    SideEffect {
        analytics.logEvent(
            analytics.event.screenView, mapOf(
                analytics.param.screenName to "AudioPlayer",
                analytics.param.screenClass to "AudioPlayerScreen"
            )
        )
    }

    LaunchedEffect(uiState.playerState) {
        if (uiState.playerState == PlayerStateUi.Ready) {
            viewModel.sendIntent(Intent.InitScreen(title, fileName))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.currentPositionMs.collect {
            progress = it
        }
    }

    AudioPlayerScreen(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        modifier = modifier,
        progress = { progress },
        isPlaying = { uiState.playerState == PlayerStateUi.Playing },
        onReply10Click = { viewModel.sendIntent(Intent.SeekTo(-10f * 1000)) },
        onForward10Click = { viewModel.sendIntent(Intent.SeekTo(10f * 1000)) },
        onPlayPauseClick = {
            if (uiState.playerState == PlayerStateUi.Playing) {
                analytics.logEvent(
                    analytics.event.pauseAudioPlayer, mapOf(
                        analytics.param.itemId to fileName,
                        analytics.param.contentType to "podcast"
                    )
                )
                viewModel.sendIntent(Intent.PauseAudio)
            } else {
                analytics.logEvent(
                    analytics.event.playAudioPlayer, mapOf(
                        analytics.param.itemId to fileName,
                        analytics.param.contentType to "podcast"
                    )
                )
                viewModel.sendIntent(Intent.PlayAudio)
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
    onReply10Click: () -> Unit,
    onForward10Click: () -> Unit,
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                KIcon.Replay10,
                null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(56.dp)
                    .clickable { onReply10Click() }
            )
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

            Icon(
                KIcon.Forward10,
                null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(56.dp)
                    .clickable { onForward10Click() })

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
            onForward10Click = {},
            onReply10Click = {}
        )
    }
}