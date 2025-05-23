package com.kesicollection.data.retrofit.model.kesiandroid

import com.kesicollection.core.model.Category
import com.kesicollection.core.model.Content
import com.kesicollection.core.model.ContentType
import com.kesicollection.core.model.Discover
import com.kesicollection.core.model.PromotedContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network representation of [Content] when fetched from /discover
 * @property id [Content.id]
 * @property img [Content.img]
 * @property type [Content.type]
 * @property title [Content.title]
 * @property description [Content.description]
 *
 */
@Serializable
data class NetworkContent(
    val id: String,
    val img: String,
    val type: NetworkContentType,
    val title: String,
    val description: String,
)

/**
 * Network representation of [ContentType]
 */
@Serializable
enum class NetworkContentType {
    @SerialName("Article")
    Article,

    @SerialName("Podcast")
    Podcast,

    @SerialName("Video")
    Video,

    @SerialName("Demo")
    Demo
}

/**
 * Network representation of [Category]
 * @property id [Category.id]
 * @property name [Category.name]
 */
@Serializable
data class NetworkCategory(
    val id: String,
    val name: String,
)

/**
 * Network representation of [PromotedContent]
 * @property category [PromotedContent.category]
 * @property promotedContent [PromotedContent.content]
 */
@Serializable
data class NetworkPromotedContent(
    val category: NetworkCategory,
    val promotedContent: List<NetworkContent>
)

/**
 * Network representation of [Discover]
 * @property featured [Discover.featured]
 * @property promotedContent [Discover.promotedContent]
 */
@Serializable
data class NetworkDiscover(
    val featured: List<NetworkContent>,
    val promotedContent: List<NetworkPromotedContent>
)

/**
 * Converts a [NetworkContent] to [Content]
 * @return [Content]
 */
fun NetworkContent.asContent(): Content {
    return Content(
        id = this.id,
        img = this.img,
        type = ContentType.valueOf(type.name),
        title = this.title,
        description = this.description
    )
}

/**
 * Converts a [NetworkCategory] to [Category]
 * @return [Category]
 */
fun NetworkCategory.asCategory(): Category {
    return Category(
        id = this.id,
        name = this.name
    )
}

/**
 * Converts a [NetworkPromotedContent] to [PromotedContent]
 * @return [PromotedContent]
 */
fun NetworkPromotedContent.asPromotedContent(): PromotedContent {
    return PromotedContent(
        category = this.category.asCategory(),
        content = this.promotedContent.map { it.asContent() }
    )
}

/**
 * Converts a [NetworkDiscover] to [Discover]
 * @return [Discover]
 */
fun NetworkDiscover.asDiscover(): Discover {
    return Discover(
        featured = this.featured.map { it.asContent() },
        promotedContent = this.promotedContent.map { it.asPromotedContent() }
    )
}