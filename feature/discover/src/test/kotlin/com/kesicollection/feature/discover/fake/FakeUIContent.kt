package com.kesicollection.feature.discover.fake

import com.kesicollection.core.model.ContentType
import com.kesicollection.feature.discover.UIContent
import kotlinx.collections.immutable.persistentListOf

object FakeUIContent {
    val items = persistentListOf(
        UIContent(
            id = "1",
            img = "jetpack_compose.png",
            type = ContentType.Article,
            title = "Exploring Jetpack Compose",
            description = "A deep dive into the modern UI toolkit for Android."
        ),
        UIContent(
            id = "2",
            img = "kmp_podcast.png",
            type = ContentType.Podcast,
            title = "The Future of Kotlin Multiplatform",
            description = "Listen to experts discuss KMP's potential."
        ),
        UIContent(
            id = "3",
            img = "accessibility_video.png",
            type = ContentType.Video,
            title = "Building Accessible Android Apps",
            description = "Learn how to make your apps usable by everyone."
        ),
        UIContent(
            id = "4",
            img = "android_studio_demo.png",
            type = ContentType.Demo,
            title = "Android Studio Tips and Tricks",
            description = "A quick demo of useful IDE features."
        ),
        UIContent(
            id = "5",
            img = "coroutines_article.png",
            type = ContentType.Article,
            title = "Mastering Coroutines in Kotlin",
            description = "An in-depth article on asynchronous programming."
        ),
        UIContent(
            id = "6",
            img = "performance_podcast.png",
            type = ContentType.Podcast,
            title = "Android Performance Patterns",
            description = "Podcast episode covering best practices for app performance."
        ),
        UIContent(
            id = "7",
            img = "material_you_video.png",
            type = ContentType.Video,
            title = "Introduction to Material Design 3",
            description = "Video tutorial on implementing Material You."
        ),
        UIContent(
            id = "8",
            img = "offline_first_demo.png",
            type = ContentType.Demo,
            title = "Offline First App Demo",
            description = "Showcasing strategies for building offline-capable apps."
        ),
        UIContent(
            id = "9",
            img = "android_testing.png",
            type = ContentType.Article,
            title = "Testing in Android: A Comprehensive Guide",
            description = "An article covering unit, integration, and UI testing."
        ),
        UIContent(
            id = "10",
            img = "android_14_podcast.png",
            type = ContentType.Podcast,
            title = "What's New in Android 14",
            description = "A podcast discussing the latest Android features."
        )
    )
}