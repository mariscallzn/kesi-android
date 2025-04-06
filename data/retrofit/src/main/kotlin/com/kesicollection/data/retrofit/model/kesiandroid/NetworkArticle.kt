package com.kesicollection.data.retrofit.model.kesiandroid

import com.kesicollection.core.model.Article
import com.kesicollection.core.model.ContentSection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a summary of an article fetched from the network, typically for display in a list or index.
 *
 * @property id The unique identifier of the article.
 * @property title The title of the article.
 * @property description A short description of the article.
 * @property thumbnail The URL of the article's thumbnail image.
 */
@Serializable
data class NetworkIndexArticle(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String,
)

/**
 * Represents an article fetched from the network.
 *
 * @property id The unique identifier of the article.
 * @property title The title of the article.
 * @property description A short description of the article.
 * @property thumbnail The URL of the article's thumbnail image.
 * @property content A list of [NetworkContentSection] representing the article's content.
 * @property podcast Optional data about a podcast related to this article.
 */
@Serializable
data class NetworkArticle(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val content: List<NetworkContentSection>,
    val podcast: NetworkPodcast?,
)

/**
 * Represents a section of content within a network article.
 * This is a sealed class that defines the different types of content sections that can be present in an article.
 * Each subclass represents a different type of content, such as a subheader, paragraph, bullet list, or code block.
 *
 * @property content The text content of the section.
 */
@Serializable
sealed class NetworkContentSection {
    abstract val content: String
}

/**
 * Represents a sub-header content section within a network article.
 *
 * @property content The text content of the sub-header.
 */
@Serializable
@SerialName("sub_header")
class SubHeader(
    override val content: String
) : NetworkContentSection()

/**
 * Represents a paragraph of text content within a [NetworkArticle].
 *
 * @property content The text content of the paragraph.
 */
@Serializable
@SerialName("paragraph")
class Paragraph(
    override val content: String
) : NetworkContentSection()

/**
 * Represents a bulleted list within the content of a network article.
 *
 * @property content The introductory text or heading for the bullet list.
 * @property bulletPoints A list of individual bullet points (strings) that make up the list.
 */
@Serializable
@SerialName("bullet_list")
class BulletList(
    override val content: String,
    val bulletPoints: List<String>
) : NetworkContentSection()

/**
 * Represents a code block within an article's content.
 *
 * @property content The code content as a String.
 */
@Serializable
@SerialName("code")
class Code(
    override val content: String
) : NetworkContentSection()

fun NetworkIndexArticle.asArticle() = Article(
    id = id,
    title = title,
    description = description,
    thumbnail = thumbnail,
    content = emptyList(),
    podcast = null,
)

/**
 * Converts a [NetworkArticle] to an [Article].
 *
 * This function maps the properties of a [NetworkArticle] to an [Article].
 * It also transforms the list of [NetworkContentSection] to [ContentSection]
 * using the [asContentSection] function and the [NetworkPodcast] to [Podcast]
 * using the [asPodcast] function.
 *
 * @return An [Article] object created from the [NetworkArticle].
 */
fun NetworkArticle.asArticle() = Article(
    id = id,
    title = title,
    description = description,
    thumbnail = thumbnail,
    content = content.map { it.asContentSection() },
    podcast = podcast?.asPodcast()
)

/**
 * Converts a [NetworkContentSection] to its corresponding [ContentSection] model.
 *
 * This function handles the different types of content sections received from the network
 * and maps them to their appropriate model representations.
 *
 * @return A [ContentSection] object representing the converted network content section.
 * @throws IllegalArgumentException if the [NetworkContentSection] type is not recognized.
 */
fun NetworkContentSection.asContentSection(): ContentSection = when (this) {
    is BulletList -> ContentSection.BulletList(content, bulletPoints)
    is Code -> ContentSection.Code(content)
    is Paragraph -> ContentSection.Paragraph(content)
    is SubHeader -> ContentSection.SubHeader(content)
}

const val lorem =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi. Proin porttitor, orci nec nonummy molestie, enim est eleifend mi, non fermentum diam nisl sit amet erat. Duis semper. Duis arcu massa, scelerisque vitae, consequat in, pretium a, enim. Pellentesque congue. Ut in risus volutpat libero pharetra tempor. Cras vestibulum bibendum augue. Praesent egestas leo in pede. Praesent blandit odio eu enim. Pellentesque sed dui ut augue blandit sodales. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Aliquam nibh. Mauris ac mauris sed pede pellentesque fermentum. Maecenas adipiscing ante non diam. Sed eget nisl."

val sections = listOf(
    SubHeader(lorem.take(56)),
    Paragraph(lorem),
    BulletList(lorem.take(45), listOf(lorem.take(70), lorem.take(33))),
    Code(
        """
```
@Serializable
sealed class ContentSectionResponse {
    abstract val content: String
}

@Serializable
@SerialName("sub_header")
class SubHeader(
    override val content: String
) : ContentSectionResponse()

@Serializable
@SerialName("paragraph")
class Paragraph(
    override val content: String
) : ContentSectionResponse()

@Serializable
@SerialName("bullet_list")
class BulletList(
    override val content: String,
    val bulletPoints: List<String>
) : ContentSectionResponse()

@Serializable
@SerialName("code")
class Code(
    override val content: String
) : ContentSectionResponse()
```""".trimIndent()
    ),
    Paragraph(lorem.take(60)),
)