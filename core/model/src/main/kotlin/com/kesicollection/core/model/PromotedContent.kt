package com.kesicollection.core.model

/**
 * Data class representing promoted content associated with a category.
 *
 * This class encapsulates a [Category] and a list of [Content] items that are
 * being promoted for that specific category. It is used to structure and manage
 * featured or highlighted content within the application.
 *
 * @property category The [Category] to which the promoted content belongs.
 * @property content A list of [Content] items that are promoted for the given category.
 */
data class PromotedContent(
    val category: Category,
    val content: List<Content>
)