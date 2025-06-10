package com.kesicollection.feature.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.uisystem.component.KCard
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.article.R
import com.kesicollection.feature.article.uimodel.UiPodcast

/**
 * A composable function that displays a card representing a podcast.
 *
 *  @sample com.kesicollection.feature.article.components.PodcastCardExample
 *
 * @param uiPodcast The unique identifier of the podcast.
 * @param onPodcastClick A lambda function that is invoked when the podcast card is clicked.
 *                       It receives the `podcastId` as a parameter.
 * @param modifier Modifier to be applied to the Row container of the card.
 */
@Composable
fun PodcastCard(
    uiPodcast: UiPodcast,
    onPodcastClick: (uiPodcast: UiPodcast) -> Unit,
    modifier: Modifier = Modifier
) {

    val rememberedOnPodcastClick = remember { { onPodcastClick(uiPodcast) } }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = rememberedOnPodcastClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        KCard {
            Icon(
                KIcon.Podcasts, stringResource(R.string.feature_article_podcast),
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(
            uiPodcast.title,
            modifier = Modifier.weight(1f),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(40.dp)
        ) {
            Icon(
                KIcon.PlayArrow,
                stringResource(R.string.feature_article_play_podcast),
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PodcastCardPreview() {
    PodcastCardExample(modifier = Modifier.padding(16.dp))
}

@Composable
private fun PodcastCardExample(modifier: Modifier = Modifier) {
    KesiTheme {
        PodcastCard(
            uiPodcast = UiPodcast(
                id = "1",
                title = "Podcast Title: This will generate a long test to show case the overflow of text",
                audio = "test"
            ),
            modifier = modifier,
            onPodcastClick = {}
        )
    }
}