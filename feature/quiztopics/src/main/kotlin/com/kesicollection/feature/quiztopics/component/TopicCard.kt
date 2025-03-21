package com.kesicollection.feature.quiztopics.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.model.Topic
import com.kesicollection.core.model.TopicCard
import com.kesicollection.core.uisystem.component.KCard
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiztopics.R

@Composable
fun TopicCard(
    topicCard: TopicCard,
    onCardClick: (TopicCard) -> Unit,
    modifier: Modifier = Modifier
) {
    KCard(
        modifier = modifier
            .clickable { onCardClick(topicCard) },
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = topicCard.topic.name,
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = pluralStringResource(
                        R.plurals.feature_quiztopics_card_description,
                        topicCard.totalQuestions,
                        topicCard.totalQuestions,
                    ),
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Icon(KIcon.ChevronRight, null)
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewTopicCard() {
    ExampleTopicCard(
        modifier = Modifier.padding(8.dp),
        topicCard = TopicCard(
            Topic(
                name = "Jetpack Compose"
            ), 35
        )
    )
}

@Composable
private fun ExampleTopicCard(
    topicCard: TopicCard,
    modifier: Modifier = Modifier
) {
    KesiTheme {
        TopicCard(
            modifier = modifier,
            topicCard = topicCard,
            onCardClick = {}
        )
    }
}