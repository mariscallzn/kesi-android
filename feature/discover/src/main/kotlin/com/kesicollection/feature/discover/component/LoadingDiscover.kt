package com.kesicollection.feature.discover.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.kesicollection.core.uisystem.modifier.ShimmerModifierDefaults
import com.kesicollection.core.uisystem.modifier.animateShimmer
import com.kesicollection.core.uisystem.theme.KesiTheme
import kotlin.random.Random

@Composable
fun LoadingDiscover(
    modifier: Modifier = Modifier
) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val repeatHorizontalTimes = when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> 5
        WindowWidthSizeClass.MEDIUM -> 10
        WindowWidthSizeClass.EXPANDED -> 15
        else -> 5
    }
    val repeatVerticalTimes = when (windowSizeClass.windowHeightSizeClass) {
        WindowHeightSizeClass.COMPACT -> 2
        WindowHeightSizeClass.MEDIUM -> 3
        WindowHeightSizeClass.EXPANDED -> 5
        else -> 2
    }

    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(repeatHorizontalTimes) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .width(Random.nextInt(92, 120).dp) //Check this could cause recomp
                        .height(24.dp)
                        .animateShimmer(ShimmerModifierDefaults.defaultColorList())
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(86.dp)
                .height(24.dp)
                .animateShimmer(ShimmerModifierDefaults.defaultColorList())
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(repeatHorizontalTimes) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(320.dp)
                        .animateShimmer(ShimmerModifierDefaults.defaultColorList())
                )
            }
        }
        repeat(repeatVerticalTimes) {
            Spacer(Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .width(86.dp)
                    .height(24.dp)
                    .animateShimmer(ShimmerModifierDefaults.defaultColorList())
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(repeatHorizontalTimes) {
                    LoadingContentCarousel()
                }
            }
        }
    }
}

@Composable
fun LoadingContentCarousel(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(240.dp)
                .height(140.dp)
                .animateShimmer(ShimmerModifierDefaults.defaultColorList())
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(200.dp)
                .height(24.dp)
                .animateShimmer(ShimmerModifierDefaults.defaultColorList())
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(86.dp)
                .height(24.dp)
                .animateShimmer(ShimmerModifierDefaults.defaultColorList())
        )
    }
}

@Composable
private fun ExampleLoadingDiscover(
    modifier: Modifier = Modifier,
) {
    KesiTheme {
        LoadingDiscover(modifier = modifier)
    }
}

@Composable
private fun ExampleLoadingContentCarousel(modifier: Modifier = Modifier) {
    KesiTheme {
        LoadingContentCarousel(modifier = modifier)
    }
}

@PreviewLightDark
@Composable
private fun PreviewLoadingDiscover() {
    ExampleLoadingDiscover(modifier = Modifier.padding(16.dp))
}

@PreviewLightDark
@Composable
private fun PreviewLoadingContentCarousel() {
    ExampleLoadingContentCarousel(modifier = Modifier.padding(16.dp))
}