package com.kesicollection.testing.testdata

import com.kesicollection.core.model.Podcast

/**
 * Test data for [Podcast] objects.
 *
 * This object provides a collection of sample [Podcast] instances that can be used
 * for testing purposes, such as UI previews, unit tests, or integration tests.
 *
 * @property items A list of [Podcast] objects representing sample podcast data.
 */
object PodcastTestData {
    val items = listOf(
        Podcast(
            id = "1",
            title = "Podcast 1",
            url = "https://www.some.url",
            image = "https://www.some.url",
        )
    )
}