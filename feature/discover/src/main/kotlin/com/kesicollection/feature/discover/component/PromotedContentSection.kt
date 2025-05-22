package com.kesicollection.feature.discover.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import com.kesicollection.core.model.ContentType
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.discover.R
import com.kesicollection.feature.discover.UICategory
import com.kesicollection.feature.discover.UIContent

fun LazyListScope.promotedContentSections(
    promotedContent: Map<UICategory, List<UIContent>>,
    onContentClick: (UIContent) -> Unit,
    modifier: Modifier = Modifier,
    onSeeAllClick: (UICategory) -> Unit = {},
) {
    promotedContent.forEach { (category, contentList) ->
        item {
            val rememberedOnSeeAllClick = remember { { onSeeAllClick(category) } }

            Column(
                modifier = modifier
                    .padding(bottom = 16.dp)
                    .fillParentMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillParentMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Promoted: ${category.name}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    )
                    TextButton(onClick = rememberedOnSeeAllClick) {
                        Text(text = stringResource(R.string.feature_discover_see_all))
                    }
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(contentList) { content ->
                        PromotedCard(
                            uiContent = content,
                            onContentClick = onContentClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PromotedCard(
    uiContent: UIContent,
    onContentClick: (UIContent) -> Unit,
    modifier: Modifier = Modifier
) {

    val rememberedOnContentClick = remember { { onContentClick(uiContent) } }

    Column(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = rememberedOnContentClick)
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(240.dp)
                .height(140.dp),
            model = uiContent.img,
            contentScale = ContentScale.Crop,
            imageLoader = LocalImageLoader.current,
            contentDescription = null,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = uiContent.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 3.dp) //Find a better way to align center of the first line
                    .size(14.dp),
                imageVector = when (uiContent.type) {
                    ContentType.Article -> KIcon.AutoStories
                    ContentType.Podcast -> KIcon.Podcasts
                    ContentType.Video -> KIcon.Movie
                    ContentType.Demo -> KIcon.TouchApp
                }, contentDescription = null
            )
            Text(
                text = uiContent.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ExamplePromotedCard(
    modifier: Modifier = Modifier,
    uiContent: UIContent = UIContent(
        id = "feat1",
        img = "debugging_jetpack_compose_img.jpg",
        type = ContentType.Article,
        title = "Featured Article 1",
        description = "Description for featured article 1."
    )
) {
    KesiTheme {
        PromotedCard(
            modifier = modifier,
            uiContent = uiContent,
            onContentClick = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewPromotedCard() {
    ExamplePromotedCard()
}