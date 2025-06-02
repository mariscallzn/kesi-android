/**
 * This file contains fake article data used for testing and development purposes.
 * It provides a list of `Article` objects, each with details such as ID, title,
 * description, image, markdown content, and an optional associated podcast.
 */
package com.kesicollection.test.core.fake

import com.kesicollection.core.model.Article
import com.kesicollection.core.model.Podcast

/**
 * `FakeArticles` is an object that holds a list of predefined [Article] instances.
 * This is useful for populating UI previews, running tests, or demonstrating
 * features without needing a live data source.
 */
object FakeArticles {
    /**
     * A list of [Article] objects representing fake articles.
     * Each article includes:
     * - `id`: A unique identifier for the article.
     * - `title`: The title of the article.
     * - `description`: A short summary of the article.
     * - `img`: The filename of the image associated with the article.
     * - `markdown`: The full content of the article in Markdown format.
     * - `podcast`: An optional [Podcast] object linked to the article.
     */
    val items = listOf(
        Article(
            id = "1",
            title = "The Future of Android Development",
            description = "Exploring upcoming trends and technologies in Android.",
            img = "android_future.png",
            markdown = """
                # The Future of Android Development
                
                Android development is constantly evolving. In this article, we explore...
                
                ## Key Trends
                - Declarative UI (Jetpack Compose)
                - Kotlin Multiplatform
                - AI/ML Integration
            """.trimIndent(),
            podcast = Podcast(
                id = "podcast1",
                title = "Android Dev Insights Ep. 1",
                audio = "android_dev_insights_ep1.mp3",
                img = "podcast_android_insights.png"
            )
        ),
        Article(
            id = "2",
            title = "Mastering Jetpack Compose",
            description = "A comprehensive guide to building beautiful UIs with Compose.",
            img = "jetpack_compose_mastery.png",
            markdown = """
                # Mastering Jetpack Compose
                
                Jetpack Compose is a modern toolkit for building native Android UI.
                
                ## Core Concepts
                - Composable functions
                - State management
                - Layouts
            """.trimIndent(),
            podcast = null
        ),
        Article(
            id = "3",
            title = "Kotlin Coroutines for Asynchronous Programming",
            description = "Learn how to simplify async code with Kotlin Coroutines.",
            img = "kotlin_coroutines.png",
            markdown = """
                # Kotlin Coroutines for Asynchronous Programming
                
                Coroutines provide a powerful way to manage background tasks.
                
                ## Benefits
                - Simplified async code
                - Structured concurrency
                - Improved performance
            """.trimIndent(),
            podcast = Podcast(
                id = "podcast2",
                title = "Async Android",
                audio = "async_android.mp3",
                img = "podcast_async_android.png"
            )
        ),
        Article(
            id = "4",
            title = "Dependency Injection with Hilt",
            description = "A practical guide to using Hilt for DI in Android.",
            img = "hilt_di.png",
            markdown = """
                # Dependency Injection with Hilt
                
                Hilt simplifies dependency injection in Android applications.
                
                ## Getting Started
                - Setup
                - Annotations
                - Scopes
            """.trimIndent(),
            podcast = null
        ),
        Article(
            id = "5",
            title = "Building Accessible Android Apps",
            description = "Best practices for creating inclusive Android applications.",
            img = "accessibility_android.png",
            markdown = """
                # Building Accessible Android Apps
                
                Ensuring your app is usable by everyone is crucial.
                
                ## Key Considerations
                - Content descriptions
                - Touch target size
                - Color contrast
            """.trimIndent(),
            podcast = Podcast(
                id = "podcast3",
                title = "Inclusive Design",
                audio = "inclusive_design.mp3",
                img = "podcast_inclusive_design.png"
            )
        ),
        Article(
            id = "6",
            title = "Testing in Android: A Comprehensive Guide",
            description = "Learn about different testing strategies and tools.",
            img = "android_testing.png",
            markdown = """
                # Testing in Android: A Comprehensive Guide
                
                Thorough testing ensures app quality and reliability.
                
                ## Types of Tests
                - Unit tests
                - Integration tests
                - UI tests
            """.trimIndent(),
            podcast = null
        ),
        Article(
            id = "7",
            title = "Introduction to Kotlin Multiplatform Mobile (KMM)",
            description = "Share code between Android and iOS with KMM.",
            img = "kmm_intro.png",
            markdown = """
                # Introduction to Kotlin Multiplatform Mobile (KMM)
                
                KMM allows you to write shared business logic in Kotlin.
                
                ## Advantages
                - Code reuse
                - Consistent logic
                - Native UI
            """.trimIndent(),
            podcast = Podcast(
                id = "podcast4",
                title = "Cross-Platform with Kotlin",
                audio = "cross_platform_kotlin.mp3",
                img = "podcast_kmm.png"
            )
        ),
        Article(
            id = "8",
            title = "Android App Architecture Best Practices",
            description = "Explore popular architectural patterns like MVVM and MVI.",
            img = "android_architecture.png",
            markdown = """
                # Android App Architecture Best Practices
                
                Choosing the right architecture is key to a scalable app.
                
                ## Popular Patterns
                - MVVM (Model-View-ViewModel)
                - MVI (Model-View-Intent)
                - Clean Architecture
            """.trimIndent(),
            podcast = null
        ),
        Article(
            id = "9",
            title = "Securing Your Android Application",
            description = "Essential tips for protecting user data and app integrity.",
            img = "android_security.png",
            markdown = """
                # Securing Your Android Application
                
                Security should be a top priority in app development.
                
                ## Best Practices
                - Data encryption
                - Network security
                - Secure coding practices
            """.trimIndent(),
            podcast = Podcast(
                id = "podcast5",
                title = "Android Security Deep Dive",
                audio = "android_security.mp3",
                img = "podcast_android_security.png"
            )
        ),
        Article(
            id = "10",
            title = "Optimizing Android App Performance",
            description = "Techniques for making your app faster and more responsive.",
            img = "android_performance.png",
            markdown = """
                # Optimizing Android App Performance
                
                A performant app leads to a better user experience.
                
                ## Areas to Optimize
                - UI rendering
                - Memory usage
                - Background tasks
            """.trimIndent(),
            podcast = null
        )
    )
}