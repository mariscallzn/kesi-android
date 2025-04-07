package com.kesicollection.testing.testdata

import com.kesicollection.core.model.Article
import java.util.UUID

/**
 * Test data for [Article] objects.
 *
 * Provides a list of 10 sample articles with predefined titles, descriptions, thumbnails,
 * content sections, and associated podcasts for testing purposes.
 *
 * The `items` property contains the list of test [Article] instances. Each article
 * is populated with sample data, including:
 * - A unique UUID for the `id`.
 * - A title in the format "Article [number]".
 * - A description in the format "Description for Article [number]".
 * - A placeholder thumbnail URL.
 * - Content sections fetched from [ContentSectionTestData.items].
 * - A podcast fetched from the first item of [PodcastTestData.items].
 */
object ArticlesTestData {
    val items = listOf(
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 1",
            description = "Description for Article 1",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        ),
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 2",
            description = "Description for Article 2",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        ),
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 3",
            description = "Description for Article 3",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        ),
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 4",
            description = "Description for Article 4",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        ),
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 5",
            description = "Description for Article 5",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        ),
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 6",
            description = "Description for Article 6",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        ),
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 7",
            description = "Description for Article 7",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        ),
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 8",
            description = "Description for Article 8",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        ),
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 9",
            description = "Description for Article 9",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        ),
        Article(
            id = UUID.randomUUID().toString(),
            title = "Article 10",
            description = "Description for Article 10",
            thumbnail = "https://www.some.url",
            content = ContentSectionTestData.items,
            podcast = PodcastTestData.items.first(),
        )
    )
}