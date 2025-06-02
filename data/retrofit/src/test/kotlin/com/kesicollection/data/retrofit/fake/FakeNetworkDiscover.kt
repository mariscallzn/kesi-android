package com.kesicollection.data.retrofit.fake

import com.kesicollection.data.retrofit.model.kesiandroid.NetworkCategory
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkContent
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkContentType
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkDiscover
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkPromotedContent

object FakeNetworkDiscover {
    val items = listOf(
        NetworkDiscover(
            featured = listOf(
                NetworkContent("featured_article_1", "img_url_1", NetworkContentType.Article, "Featured Article 1", "Description for featured article 1"),
                NetworkContent("featured_video_1", "img_url_2", NetworkContentType.Video, "Featured Video 1", "Description for featured video 1")
            ),
            promotedContent = listOf(
                NetworkPromotedContent(
                    category = NetworkCategory("cat_tech", "Technology"),
                    promotedContent = listOf(
                        NetworkContent("tech_podcast_1", "img_url_3", NetworkContentType.Podcast, "Tech Podcast Episode 1", "Exploring new gadgets"),
                        NetworkContent("tech_article_1", "img_url_4", NetworkContentType.Article, "Future of AI", "An in-depth look at AI development")
                    )
                ),
                NetworkPromotedContent(
                    category = NetworkCategory("cat_lifestyle", "Lifestyle"),
                    promotedContent = listOf(
                        NetworkContent("lifestyle_video_1", "img_url_5", NetworkContentType.Video, "Healthy Living Tips", "Quick tips for a healthier life")
                    )
                )
            )
        ),
        NetworkDiscover(
            featured = listOf(
                NetworkContent("featured_podcast_2", "img_url_6", NetworkContentType.Podcast, "Featured Podcast 2", "Interviews with innovators")
            ),
            promotedContent = listOf(
                NetworkPromotedContent(
                    category = NetworkCategory("cat_science", "Science"),
                    promotedContent = listOf(
                        NetworkContent("science_article_2", "img_url_7", NetworkContentType.Article, "Space Exploration Update", "Latest news from NASA"),
                        NetworkContent("science_demo_1", "img_url_8", NetworkContentType.Demo, "Interactive Physics Demo", "Experience physics principles")
                    )
                )
            )
        ),
        NetworkDiscover( // Item 3
            featured = listOf(
                NetworkContent("featured_demo_1", "img_url_9", NetworkContentType.Demo, "Featured Demo: VR Experience", "Try our new VR demo")
            ),
            promotedContent = emptyList()
        ),
        NetworkDiscover( // Item 4
            featured = emptyList(),
            promotedContent = listOf(
                NetworkPromotedContent(
                    category = NetworkCategory("cat_business", "Business"),
                    promotedContent = listOf(
                        NetworkContent("biz_article_1", "img_url_10", NetworkContentType.Article, "Startup Success Stories", "Learn from the best")
                    )
                )
            )
        ),
        NetworkDiscover( // Item 5
            featured = listOf(
                NetworkContent("featured_article_3", "img_url_11", NetworkContentType.Article, "The Art of Storytelling", "Crafting compelling narratives")
            ),
            promotedContent = listOf(
                NetworkPromotedContent(
                    category = NetworkCategory("cat_arts", "Arts & Culture"),
                    promotedContent = listOf(
                        NetworkContent("arts_video_1", "img_url_12", NetworkContentType.Video, "Museum Tour: Modern Art", "A virtual tour"),
                        NetworkContent("arts_podcast_1", "img_url_13", NetworkContentType.Podcast, "Creative Minds Speak", "Insights from artists")
                    )
                ),
                NetworkPromotedContent(
                    category = NetworkCategory("cat_tech", "Technology"), // Re-using a category
                    promotedContent = listOf(
                        NetworkContent("tech_demo_2", "img_url_14", NetworkContentType.Demo, "New Software Demo", "Check out the latest features")
                    )
                )
            )
        ),
        NetworkDiscover( // Item 6
            featured = listOf(
                NetworkContent("featured_video_2", "img_url_15", NetworkContentType.Video, "Documentary: Nature's Wonders", "Exploring wildlife")
            ),
            promotedContent = listOf(
                NetworkPromotedContent(
                    category = NetworkCategory("cat_education", "Education"),
                    promotedContent = listOf(
                        NetworkContent("edu_article_1", "img_url_16", NetworkContentType.Article, "The Importance of Lifelong Learning", "Stay curious")
                    )
                )
            )
        ),
        NetworkDiscover( // Item 7
            featured = listOf(
                NetworkContent("featured_podcast_3", "img_url_17", NetworkContentType.Podcast, "Weekly Tech Roundup", "News and analysis")
            ),
            promotedContent = listOf(
                NetworkPromotedContent(
                    category = NetworkCategory("cat_gaming", "Gaming"),
                    promotedContent = listOf(
                        NetworkContent("game_video_1", "img_url_18", NetworkContentType.Video, "Top 5 Indie Games", "Hidden gems to play"),
                        NetworkContent("game_article_1", "img_url_19", NetworkContentType.Article, "The Evolution of Game Design", "From pixels to photorealism")
                    )
                )
            )
        ),
        NetworkDiscover( // Item 8
            featured = listOf(
                NetworkContent("featured_article_4", "img_url_20", NetworkContentType.Article, "Mindfulness and Well-being", "Techniques for a calmer life")
            ),
            promotedContent = listOf(
                NetworkPromotedContent(
                    category = NetworkCategory("cat_health", "Health & Wellness"),
                    promotedContent = listOf(
                        NetworkContent("health_podcast_1", "img_url_21", NetworkContentType.Podcast, "Nutrition Explained", "Expert advice on eating well")
                    )
                )
            )
        ),
        NetworkDiscover( // Item 9
            featured = listOf(
                NetworkContent("featured_demo_2", "img_url_22", NetworkContentType.Demo, "AI Chatbot Demo", "Interact with our new AI")
            ),
            promotedContent = listOf(
                NetworkPromotedContent(
                    category = NetworkCategory("cat_diy", "DIY & Crafts"),
                    promotedContent = listOf(
                        NetworkContent("diy_video_1", "img_url_23", NetworkContentType.Video, "Home Decor DIY Project", "Easy and affordable ideas")
                    )
                )
            )
        ),
        NetworkDiscover( // Item 10
            featured = listOf(
                NetworkContent("featured_video_3", "img_url_24", NetworkContentType.Video, "Travel Vlog: Hidden Gems", "Exploring offbeat destinations")
            ),
            promotedContent = listOf(
                NetworkPromotedContent(
                    category = NetworkCategory("cat_travel", "Travel"),
                    promotedContent = listOf(
                        NetworkContent("travel_article_1", "img_url_25", NetworkContentType.Article, "Budget Travel Tips", "See the world without breaking the bank"),
                        NetworkContent("travel_podcast_1", "img_url_26", NetworkContentType.Podcast, "Adventures Abroad", "Stories from fellow travelers")
                    )
                ),
                NetworkPromotedContent(
                    category = NetworkCategory("cat_food", "Food & Cooking"),
                    promotedContent = listOf(
                        NetworkContent("food_video_1", "img_url_27", NetworkContentType.Video, "Quick Recipes: Weeknight Dinners", "Delicious and easy meals")
                    )
                )
            )
        )
    )
}