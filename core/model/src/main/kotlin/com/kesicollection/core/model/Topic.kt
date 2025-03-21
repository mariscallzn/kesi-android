package com.kesicollection.core.model

import java.util.UUID

/**
 * Represents a topic with a unique identifier and a name.
 *
 * @property id The unique identifier of the topic. If not provided, a random UUID will be generated.
 * @property name The name of the topic.
 *
 * @constructor Creates a Topic object with the specified id and name.
 *              If id is not provided, a random UUID will be used.
 *
 */
data class Topic(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
)

data class TopicCard(
    val topic: Topic,
    val totalQuestions: Int
)
