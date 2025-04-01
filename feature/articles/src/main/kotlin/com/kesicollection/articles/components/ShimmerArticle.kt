package com.kesicollection.articles.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kesicollection.core.uisystem.modifier.ShimmerModifierDefaults
import com.kesicollection.core.uisystem.modifier.animateShimmer

/**
 * A composable function that displays a shimmer loading effect for an article.
 *
 * This function creates a placeholder layout resembling an article, with shimmering
 * animations applied to its elements to indicate loading in progress. It consists of
 * two text-like placeholders on the left and an image-like placeholder on the right.
 *
 * @param modifier Modifier for styling and positioning the shimmer article layout.
 */
@Composable
fun ShimmerArticle(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.then(
            Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth()
                    .animateShimmer(ShimmerModifierDefaults.defaultColorList())
            )
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth()
                    .animateShimmer(ShimmerModifierDefaults.defaultColorList())
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5))
                .animateShimmer(ShimmerModifierDefaults.defaultColorList())
                .height(80.dp)
                .width(120.dp),
        )
    }
}