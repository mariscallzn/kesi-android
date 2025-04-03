package com.kesicollection.feature.audioplayer

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.uisystem.component.KCard
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme

@Composable
fun AudioPlayerScreen(
    title: String,
    url: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(Intent.InitScreen(title))
    }

    AudioPlayerScreen(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        modifier = modifier,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AudioPlayerScreen(
    uiState: UiAudioPlayerState,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
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
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Box(
            modifier = Modifier
                .padding(bottom = safeContent.calculateBottomPadding() + 16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground)
                .size(76.dp)
        ) {
            Icon(
                KIcon.PlayArrow,
                null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
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
            onNavigateUp = {}
        )
    }
}