package com.kesicollection.feature.discover.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import com.kesicollection.core.model.ContentType
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.discover.R
import com.kesicollection.feature.discover.UIContent

fun LazyListScope.featuredContentSection(
    key: String,
    contentType: String,
    featuredContent: List<UIContent>,
    onContentClick: (UIContent) -> Unit,
    modifier: Modifier = Modifier
) {
    item(
        key = key,
        contentType = contentType
    ) {
        Column(modifier = modifier.padding(bottom = 16.dp)) {
            Text(
                text = stringResource(R.string.feature_discover_featured),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyRow(
                modifier = featuredLazyRowModifier,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(featuredContent, key = { it.id }) { content ->
                    FeaturedCard(
                        uiContent = content,
                        onContentClick = onContentClick,
                        modifier = featuredModifier
                    )
                }
            }
        }
    }
}

val featuredModifier = Modifier
    .size(320.dp)
    .clip(RoundedCornerShape(8.dp))

val featuredLazyRowModifier = Modifier
    .testTag(":feature:discover:featuredLazyRow")

@Composable
fun FeaturedCard(
    uiContent: UIContent,
    onContentClick: (UIContent) -> Unit,
    modifier: Modifier = Modifier
) {

    val rememberedOnContentClick = remember { { onContentClick(uiContent) } }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = rememberedOnContentClick)
    ) {
        AsyncImage(
            modifier = Modifier.matchParentSize(),
            model = uiContent.img,
            contentScale = ContentScale.Crop,
            imageLoader = LocalImageLoader.current,
            contentDescription = null,
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(alpha = 0.8f),
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = uiContent.title,
                    color = Color.White,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineLarge
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(14.dp),
                        tint = Color.White,
                        imageVector = when (uiContent.type) {
                            ContentType.Article -> KIcon.AutoStories
                            ContentType.Podcast -> KIcon.Podcasts
                            ContentType.Video -> KIcon.Movie
                            ContentType.Demo -> KIcon.TouchApp
                        }, contentDescription = null
                    )
                    Text(
                        text = uiContent.description,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ExampleFeaturedCard(
    modifier: Modifier = Modifier,
    uiContent: UIContent = UIContent(
        id = "feat1",
        img = "debugging_jetpack_compose_img.jpg",
        type = ContentType.Article,
        title = "Featured Article 1: One more line",
        description = "Description for featured article 1."
    ),
) {
    KesiTheme {
        FeaturedCard(
            uiContent = uiContent,
            onContentClick = {},
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun PreviewFeaturedCard() {
    ExampleFeaturedCard(
        modifier = Modifier
            .size(320.dp)
    )
}
