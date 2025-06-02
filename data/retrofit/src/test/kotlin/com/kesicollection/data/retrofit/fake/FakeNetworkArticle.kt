package com.kesicollection.data.retrofit.fake

import com.kesicollection.data.retrofit.model.kesiandroid.NetworkArticle
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkPodcast

object FakeNetworkArticle {

    val items = listOf(
        NetworkArticle(
            id = "1",
            title = "Article 1 Title",
            description = "Description for article 1.",
            img = "https://example.com/1.jpg",
            markdown = "## Article 1 Content\n\nThis is the markdown content for article 1.",
            podcast = NetworkPodcast(
                id = "podcast1",
                title = "Podcast for Article 1",
                audio = "https://example.com/1.mp3",
                img = "https://example.com/podcast_image1.jpg"
            )
        ),
        NetworkArticle(
            id = "2",
            title = "Article 2 Title",
            description = "Description for article 2.",
            img = "https://example.com/2.jpg",
            markdown = "## Article 2 Content\n\nThis is the markdown content for article 2.",
            podcast = null
        ),
        NetworkArticle(
            id = "3",
            title = "Article 3 Title",
            description = "Description for article 3.",
            img = "https://example.com/3.jpg",
            markdown = "## Article 3 Content\n\nThis is the markdown content for article 3.",
            podcast = NetworkPodcast(
                id = "podcast3",
                title = "Podcast for Article 3",
                audio = "https://example.com/3.mp3",
                img = "https://example.com/podcast_image3.jpg"
            )
        ),
        NetworkArticle(
            id = "4",
            title = "Article 4 Title",
            description = "Description for article 4.",
            img = "https://example.com/4.jpg",
            markdown = "## Article 4 Content\n\nThis is the markdown content for article 4.",
            podcast = null
        ),
        NetworkArticle(
            id = "5",
            title = "Article 5 Title",
            description = "Description for article 5.",
            img = "https://example.com/5.jpg",
            markdown = "## Article 5 Content\n\nThis is the markdown content for article 5.",
            podcast = NetworkPodcast(
                id = "podcast5",
                title = "Podcast for Article 5",
                audio = "https://example.com/5.mp3",
                img = "https://example.com/podcast_image5.jpg"
            )
        ),
        NetworkArticle(
            id = "6",
            title = "Article 6 Title",
            description = "Description for article 6.",
            img = "https://example.com/6.jpg",
            markdown = "## Article 6 Content\n\nThis is the markdown content for article 6.",
            podcast = null
        ),
        NetworkArticle(
            id = "7",
            title = "Article 7 Title",
            description = "Description for article 7.",
            img = "https://example.com/7.jpg",
            markdown = "## Article 7 Content\n\nThis is the markdown content for article 7.",
            podcast = NetworkPodcast(
                id = "podcast7",
                title = "Podcast for Article 7",
                audio = "https://example.com/7.mp3",
                img = "https://example.com/podcast_image7.jpg"
            )
        ),
        NetworkArticle(
            id = "8",
            title = "Article 8 Title",
            description = "Description for article 8.",
            img = "https://example.com/8.jpg",
            markdown = "## Article 8 Content\n\nThis is the markdown content for article 8.",
            podcast = null
        ),
        NetworkArticle(
            id = "9",
            title = "Article 9 Title",
            description = "Description for article 9.",
            img = "https://example.com/9.jpg",
            markdown = "## Article 9 Content\n\nThis is the markdown content for article 9.",
            podcast = NetworkPodcast(
                id = "podcast9",
                title = "Podcast for Article 9",
                audio = "https://example.com/9.mp3",
                img = "https://example.com/podcast_image9.jpg"
            )
        ),
        NetworkArticle(
            id = "10",
            title = "Article 10 Title",
            description = "Description for article 10.",
            img = "https://example.com/10.jpg",
            markdown = "## Article 10 Content\n\nThis is the markdown content for article 10.",
            podcast = null
        )
    )
}