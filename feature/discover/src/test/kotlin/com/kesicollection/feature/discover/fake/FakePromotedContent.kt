package com.kesicollection.feature.discover.fake

import com.kesicollection.core.model.ContentType // Assuming ContentType is in this package
import com.kesicollection.feature.discover.UICategory
import com.kesicollection.feature.discover.UIContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

object FakePromotedContent {
    val items = persistentMapOf(
        UICategory("category_1", "Category 1") to createFakeUIContentList(1, 5),
        UICategory("category_2", "Category 2") to createFakeUIContentList(2, 6),
        UICategory("category_3", "Category 3") to createFakeUIContentList(3, 5)
    )
}

private fun createFakeUIContentList(categoryNumber: Int, numberOfItems: Int): ImmutableList<UIContent> {
    return persistentListOf<UIContent>().builder().apply {
        repeat(numberOfItems) { itemIndex ->
            add(
                UIContent(
                    id = "cat${categoryNumber}_content_${itemIndex + 1}",
                    img = "image_cat${categoryNumber}_${itemIndex + 1}.png",
                    type = ContentType.entries.toTypedArray().random(),
                    title = "Title for Content ${itemIndex + 1} in Category $categoryNumber",
                    description = "Description for content ${itemIndex + 1} of category $categoryNumber. Lorem ipsum dolor sit amet."
                )
            )
        }
    }.build()
}