package com.kesicollection.feature.article.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.kesicollection.core.uisystem.modifier.ShimmerModifierDefaults
import com.kesicollection.core.uisystem.modifier.animateShimmer
import com.kesicollection.core.uisystem.theme.KesiTheme

@Composable
fun LoadingArticle(
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .animateShimmer(ShimmerModifierDefaults.defaultColorList())
        )

        Spacer(Modifier.height(8.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .height(
                    when (windowSizeClass.windowWidthSizeClass) {
                        WindowWidthSizeClass.EXPANDED -> 320.dp
                        else -> 210.dp
                    }
                )
                .animateShimmer(ShimmerModifierDefaults.defaultColorList())
        )

        repeat(10) {
            Spacer(Modifier.height(8.dp))

            repeat(3) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .animateShimmer(ShimmerModifierDefaults.defaultColorList())
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun LoadingArticlePreview() {
    LoadingArticleExample()
}

@Composable
private fun LoadingArticleExample(
    modifier: Modifier = Modifier
) {
    KesiTheme {
        LoadingArticle(modifier = modifier)
    }
}