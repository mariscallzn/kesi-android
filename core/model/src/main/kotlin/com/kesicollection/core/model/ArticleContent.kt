package com.kesicollection.core.model

data class ArticleContent(
    val articleId: String,
    val title: String,
    val image: String,
    val podcast: Podcast,
    val content: List<ContentSection>
)

sealed interface ContentSection {
    val content: String

    data class SubHeader(
        override val content: String
    ) : ContentSection

    data class Paragraph(
        override val content: String
    ) : ContentSection

    data class BulletList(
        override val content: String,
        val bulletPoints: List<String>
    ) : ContentSection

    data class Code(
        override val content: String
    ) : ContentSection
}