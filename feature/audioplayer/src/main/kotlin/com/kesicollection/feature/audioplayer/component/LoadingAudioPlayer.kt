package com.kesicollection.feature.audioplayer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.uisystem.modifier.ShimmerModifierDefaults
import com.kesicollection.core.uisystem.modifier.animateShimmer
import com.kesicollection.core.uisystem.theme.KesiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingAudioPlayer(
    modifier: Modifier = Modifier
) {
    val safeContent = WindowInsets.safeContent.asPaddingValues()
    val shimmerColors = ShimmerModifierDefaults.defaultColorList()

    Column(
        modifier = modifier.fillMaxWidth(), // Ensure the column takes full width for centering
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // This pushes TopAppBar to top and Controls to bottom
    ) {
        // Content Column (Image, Title, Progress)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            // In AudioPlayerScreen, this Column has Arrangement.spacedBy(32.dp)
            // and the Text has an additional padding(horizontal = 16.dp)
            // The LinearProgressIndicator is directly after the Text.
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(horizontal = 16.dp) // Matches padding on Text and overall content area
        ) {
            // Placeholder for "Under Construction" KCard - Assuming it's the first item if present
            // From AudioPlayerScreen, KCard with Text "\uD83D\uDEA7 This screen is under construction \uD83D\uDEA7"
            // Let's assume a similar height for the placeholder
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth(0.8f) // Approximate width
                    .height(40.dp)      // Approximate height of the text card
                    .animateShimmer(shimmerColors)
            )
            // Spacer(Modifier.height(32.dp)) // This space is handled by Arrangement.spacedBy

            // Placeholder for the Podcast Image KCard
            // From AudioPlayerScreen, Box inside KCard is .size(250.dp)
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .animateShimmer(shimmerColors)
            )
            // Spacer(Modifier.height(32.dp)) // This space is handled by Arrangement.spacedBy

            // Placeholder for the Title Text
            // From AudioPlayerScreen, Text("uiState.title", ..., modifier = Modifier.padding(horizontal = 16.dp))
            // The 16.dp horizontal padding is already on the parent Column.
            // We need to estimate height for one line of text.
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .width(200.dp) // Approximate width of the title
                    .height(24.dp) // Approximate height for a single line of text
                    .animateShimmer(shimmerColors)
            )
            // Spacer(Modifier.height(32.dp)) // This space is handled by Arrangement.spacedBy

            // Placeholder for the LinearProgressIndicator
            // From AudioPlayerScreen, LinearProgressIndicator(progress = progress)
            // It's directly after the title Text. The 32.dp spacing from Arrangement.spacedBy
            // will apply between the title Box and this progress Box.
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxWidth() // Matches LinearProgressIndicator
                    .height(4.dp)   // Standard height for LinearProgressIndicator
                    .animateShimmer(shimmerColors)
            )
        }

        // Controls Row (Replay, Play/Pause, Forward)
        Row(
            // In AudioPlayerScreen:
            // horizontalArrangement = Arrangement.spacedBy(16.dp)
            // verticalAlignment = Alignment.CenterVertically
            // Play/Pause Box has Modifier.padding(bottom = safeContent.calculateBottomPadding() + 16.dp)
            // The other icons do not have this bottom padding directly, but the Row itself should be padded.
            modifier = Modifier
                .padding(bottom = safeContent.calculateBottomPadding() + 16.dp) // Apply overall bottom padding to the Row
                .padding(horizontal = 16.dp), // Add horizontal padding to match potential screen edges
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for Replay 10 Icon
            // From AudioPlayerScreen, Icon(..., modifier = replay10Modifier) which is .size(56.dp)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(56.dp)
                    .animateShimmer(shimmerColors)
            )

            // Placeholder for Play/Pause Button
            // From AudioPlayerScreen, Box(..., .size(76.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(76.dp)
                    .animateShimmer(shimmerColors)
            )

            // Placeholder for Forward 10 Icon
            // From AudioPlayerScreen, Icon(..., modifier = forward10Modifier) which is .size(56.dp)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(56.dp)
                    .animateShimmer(shimmerColors)
            )
        }
    }
}

@Composable
private fun ExampleLoadingAudioPlayer(
    modifier: Modifier = Modifier
) {
    KesiTheme {
        LoadingAudioPlayer(modifier = modifier.fillMaxSize()) // Use fillMaxSize for preview consistency
    }
}

@PreviewLightDark
@Composable
private fun PreviewLoadingAudioPlayer() {
    ExampleLoadingAudioPlayer()
}