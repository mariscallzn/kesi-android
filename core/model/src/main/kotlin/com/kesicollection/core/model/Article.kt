package com.kesicollection.core.model

/**
 * Represents an article with its essential information.
 *
 * @property id The unique identifier of the article.
 * @property title The title of the article.
 * @property description A brief description or summary of the article's content.
 * @property thumbnail The URL or path to the article's thumbnail image.
 * @property content A list of [ContentSection] that make up the article's body.
 * @property podcast The associated [Podcast] if any.
 */
data class Article(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val content: List<ContentSection>,
    val podcast: Podcast?,
)

/**
 * Represents a section of content within an [Article].
 *
 * This sealed interface allows for different types of content sections,
 * each with its own specific structure.
 *
 * @property content The primary textual content of this section.
 */
sealed interface ContentSection {
    val content: String

    /**
     * Represents a sub-header within the article's content.
     *
     * @property content The text content of the sub-header.
     */
    data class SubHeader(
        override val content: String
    ) : ContentSection

    /**
     * Represents a paragraph of text within an article's content.
     *
     * @property content The text content of the paragraph.
     */
    data class Paragraph(
        override val content: String
    ) : ContentSection

    /**
     * Represents a section of content that is a bulleted list.
     *
     * @property content The introductory text or context for the bulleted list.
     * @property bulletPoints The list of bullet points within this section.
     */
    data class BulletList(
        override val content: String,
        val bulletPoints: List<String>
    ) : ContentSection

    /**
     * Represents a code snippet within an article's content.
     *
     * @property content The code snippet itself, as a string.
     */
    data class Code(
        override val content: String
    ) : ContentSection
}
