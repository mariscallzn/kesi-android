package com.kesicollection.feature.discover

import com.kesicollection.core.model.Category
import com.kesicollection.core.model.Content
import com.kesicollection.core.model.ContentType
import com.kesicollection.core.model.Discover
import com.kesicollection.core.model.ErrorState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap

/**
 * The initial state of the Discover screen.
 */
val initialState = UiState.Loading

/**
 * Represents the different states of the Discover screen.
 */
sealed interface UiState {
    /**
     * Represents the loading state.
     */
    data object Loading : UiState

    /**
     * Represents the state where discover content is available.
     *
     * @property featuredContent The list of featured content.
     * @property promotedContent A map of promoted content, where the key is the category and the value is a list of content.
     */
    data class DiscoverContent(
        val featuredContent: ImmutableList<UIContent> = persistentListOf(),
        val promotedContent: ImmutableMap<UICategory, ImmutableList<UIContent>> = persistentMapOf(),
    ) : UiState

    /**
     * Represents the error state.
     *
     * @property error The error state, which includes a specific [DiscoverErrors] type.
     */
    data class Error(
        val error: ErrorState<DiscoverErrors>
    ) : UiState
}

/**
 * Represents a category in the UI.
 *
 * @property id The unique identifier of the category.
 * @property name The name of the category.
 */
data class UICategory(
    val id: String,
    val name: String,
)

/**
 * Represents content in the UI.
 *
 * @property id The unique identifier of the content.
 * @property img The URL or resource identifier for the content's image.
 * @property type The type of the content (e.g., article, video).
 * @property title The title of the content.
 * @property description A brief description of the content.
 */
data class UIContent(
    val id: String,
    val img: String,
    val type: ContentType,
    val title: String,
    val description: String,
)

/**
 * Converts a [Category] domain model to a [UICategory] UI model.
 *
 * @return The corresponding [UICategory].
 */
fun Category.asUiContent() = UICategory(
    id = id,
    name = name
)

/**
 * Converts a [Content] domain model to a [UIContent] UI model.
 *
 * @return The corresponding [UIContent].
 */
fun Content.asUiContent() = UIContent(
    id = id,
    img = img,
    type = type,
    title = title,
    description = description
)

/**
 * Converts a [Discover] domain model to a [UiState.DiscoverContent] UI state.
 *
 * This function maps the featured and promoted content from the domain model
 * to their respective UI model representations ([UIContent] and [UICategory]).
 *
 * @return A [UiState.DiscoverContent] instance populated with the converted content.
 */
fun Discover.asDiscoverContent() = UiState.DiscoverContent(
    featuredContent = persistentListOf(*featured.map { it.asUiContent() }.toTypedArray()),
    promotedContent = promotedContent.associate { (category, contentList) ->
        category.asUiContent() to persistentListOf(
            *contentList.map { it.asUiContent() }.toTypedArray()
        )
    }.toImmutableMap()
)

/**
 * Enum representing the possible errors that can occur on the Discover screen.
 */
enum class DiscoverErrors {
    /**
     * A generic error that doesn't fit into other categories.
     */
    GenericError,

    /**
     * An error related to network connectivity.
     */
    NetworkError,
}

/**
 * Represents the user intents or actions that can be performed on the Discover screen.
 */
sealed interface Intent {
    /** An intent to fetch the featured items. */
    data object FetchFeatureItems : Intent
}
