package com.kesicollection.articles.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a loading state for a list of articles.
 * It uses a shimmer effect to simulate loading content.
 *
 * @param modifier The modifier to be applied to the column containing the loading indicators.
 */
@Composable
fun LoadingArticles(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        List(15) { index ->
            ShimmerArticle()
            if (index < 14) {
                Box(
                    Modifier
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                        .height(1.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}