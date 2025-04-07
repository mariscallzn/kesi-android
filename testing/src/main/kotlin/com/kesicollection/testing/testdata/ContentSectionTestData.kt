package com.kesicollection.testing.testdata

import com.kesicollection.core.model.ContentSection

/**
 * Provides test data for [ContentSection] objects.
 *
 * This object contains predefined instances of different content section types,
 * including subheaders, paragraphs, code blocks, and bullet lists, which are
 * intended for use in testing scenarios.
 *
 * The data is set up for testing different types of content sections and can be used
 * to test rendering or processing of such content.
 *
 * It has single element test lists of each type, and a list named `items` that contains one of each element 5 times.
 *
 * @property subHeaderItems A list containing a single [ContentSection.SubHeader] instance for testing.
 * @property paragraphItems A list containing a single [ContentSection.Paragraph] instance for testing.
 * @property codeItems A list containing a single [ContentSection.Code] instance for testing.
 * @property bulletListItems A list containing a single [ContentSection.BulletList] instance for testing.
 * @property items A list composed of subheader, paragraph, code and bullet list repeated 5 times.
 */
object ContentSectionTestData {
    private const val SUB_HEADER_TEXT = "SubHeader Test Data"
    private const val PARAGRAPH_TEXT = "Paragraph Test Data"
    private const val CODE_TEXT = "Code Test Data"
    private const val BULLET_LIST_TITLE = "Bullet List Test Data"
    private val BULLET_LIST_TEXT = List(4) { index -> "Bullet Point ${index + 1}" }

    val subHeaderItems = listOf(
        ContentSection.SubHeader(SUB_HEADER_TEXT)
    )
    val paragraphItems = listOf(
        ContentSection.Paragraph(PARAGRAPH_TEXT)
    )
    val codeItems = listOf(
        ContentSection.Code(CODE_TEXT)
    )
    val bulletListItems = listOf(
        ContentSection.BulletList(BULLET_LIST_TITLE, BULLET_LIST_TEXT)
    )
    val items = List(5) {
        listOf(
            subHeaderItems.first(),
            paragraphItems.first(),
            codeItems.first(),
            bulletListItems.first()
        )
    }.flatten()
}