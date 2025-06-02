package com.kesicollection.test.core.fake

import com.kesicollection.core.model.Category
import com.kesicollection.core.model.Content
import com.kesicollection.core.model.ContentType
import com.kesicollection.core.model.Discover
import com.kesicollection.core.model.PromotedContent

/**
 * Object providing fake data for Discover items.
 * This is used for testing and development purposes to simulate the data
 * that would typically be fetched from a backend or database.
 */
object FakeDiscover {
    /**
     * A list of Discover items, each populated with fake data.
     * The list contains 10 Discover items by default.
     */
    val items: List<Discover> = List(10) { index ->
        // Base identifiers for content and category to ensure uniqueness.
        val contentIdBase = "content_${index}_"
        val categoryIdBase = "category_${index}_"

        // Create a Discover item.
        Discover(
            /**
             * A list of featured content items.
             * Each Discover item has 3 featured content items.
             */
            featured = List(3) { featIndex ->
                Content(
                    id = "${contentIdBase}featured_$featIndex",
                    img = "${contentIdBase}featured_$featIndex.jpg",
                    // Assign a random content type from the available ContentType enum entries.
                    type = ContentType.entries.toTypedArray().random(),
                    title = "Featured Title ${index}_$featIndex",
                    description = "This is a detailed description for featured content ${index}_$featIndex."
                )
            },
            /**
             * A list of promoted content sections.
             * Each Discover item has 2 promoted content sections.
             */
            promotedContent = List(2) { promoIndex ->
                // Create a Category for the promoted content section.
                val category = Category(
                    id = "${categoryIdBase}promo_$promoIndex",
                    name = "Category ${index}_$promoIndex"
                )
                // Create a PromotedContent item.
                PromotedContent(
                    category = category,
                    /**
                     * A list of content items within this promoted section.
                     * Each promoted section has 2 content items.
                     */
                    content = List(2) { contentPromoIndex ->
                        Content(
                            id = "${contentIdBase}promoted_${promoIndex}_$contentPromoIndex",
                            img = "${contentIdBase}promoted_${promoIndex}_$contentPromoIndex.jpg",
                            type = ContentType.entries.toTypedArray().random(),
                            title = "Promoted Content ${index}_${promoIndex}_$contentPromoIndex for ${category.name}",
                            description = "Description for promoted item ${index}_${promoIndex}_$contentPromoIndex under ${category.name}."
                        )
                    }
                )
            }
        )
    }
}