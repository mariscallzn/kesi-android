package com.kesicollection.test.core.fake

import com.kesicollection.core.model.Podcast

/**
 * Object containing a list of fake [Podcast] items.
 */
object FakePodcasts {
    /** A list of fake [Podcast] items. */
    val items = listOf(
        Podcast(
            id = "1",
            title = "Podcast 1",
            audio = "id_1.pm3",
            img = "id_1.jpg",
        )
    )
}