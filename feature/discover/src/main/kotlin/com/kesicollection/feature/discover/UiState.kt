package com.kesicollection.feature.discover

import com.kesicollection.core.model.ContentType
import com.kesicollection.core.model.ErrorState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

val initialState = UiState.Loading

val contentSample = UiState.DiscoverContent(
    categories = persistentListOf(
        UICategory(id = "fav", name = "Favorites"),
        UICategory(id = "arch", name = "Architecture"),
        UICategory(id = "ui", name = "UI"),
        UICategory(id = "core", name = "Core"),
        UICategory(id = "nav", name = "Navigation"),
    ),
    featuredContent = persistentListOf(
        UIContent(
            id = "feat1",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Article,
            title = "Featured Article 1",
            description = "Description for featured article 1."
        ),
        UIContent(
            id = "feat2",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Video,
            title = "Featured Video 2",
            description = "Description for featured video 2."
        ),
        UIContent(
            id = "feat3",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Podcast,
            title = "Featured Podcast 3",
            description = "Description for featured podcast 3."
        ),
        UIContent(
            id = "feat4",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Article,
            title = "Featured Article 4",
            description = "Description for featured article 4."
        ),
        UIContent(
            id = "feat5",
            img = "debugging_jetpack_compose_img.jpg",
            type = ContentType.Video,
            title = "Featured Video 5",
            description = "Description for featured video 5."
        ),
    ),
    promotedContent = persistentMapOf(
        UICategory(id = "arch", name = "Architecture") to persistentListOf(
            UIContent(
                id = "arch_promo1",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "Arch Promo 1",
                description = "Desc Arch Promo 1"
            ),
            UIContent(
                id = "arch_promo2",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "Arch Promo 2",
                description = "Desc Arch Promo 2"
            ),
            UIContent(
                id = "arch_promo3",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "Arch Promo 3",
                description = "Desc Arch Promo 3"
            ),
            UIContent(
                id = "arch_promo4",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "Arch Promo 4",
                description = "Desc Arch Promo 4"
            ),
            UIContent(
                id = "arch_promo5",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "Arch Promo 5",
                description = "Desc Arch Promo 5"
            ),
        ),
        UICategory(id = "ui", name = "UI") to persistentListOf(
            UIContent(
                id = "ui_promo1",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "UI Promo 1",
                description = "Desc UI Promo 1"
            ),
            UIContent(
                id = "ui_promo2",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "UI Promo 2",
                description = "Desc UI Promo 2"
            ),
            UIContent(
                id = "ui_promo3",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "UI Promo 3",
                description = "Desc UI Promo 3"
            ),
            UIContent(
                id = "ui_promo4",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "UI Promo 4",
                description = "Desc UI Promo 4"
            ),
            UIContent(
                id = "ui_promo5",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "UI Promo 5",
                description = "Desc UI Promo 5"
            ),
        ),
        UICategory(id = "core", name = "Core") to persistentListOf(
            UIContent(
                id = "core_promo1",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "Core Promo 1",
                description = "Desc Core Promo 1"
            ),
            UIContent(
                id = "core_promo2",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "Core Promo 2",
                description = "Desc Core Promo 2"
            ),
            UIContent(
                id = "core_promo3",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Video,
                title = "Core Promo 3",
                description = "Desc Core Promo 3"
            ),
            UIContent(
                id = "core_promo4",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Podcast,
                title = "Core Promo 4",
                description = "Desc Core Promo 4"
            ),
            UIContent(
                id = "core_promo5",
                img = "debugging_jetpack_compose_img.jpg",
                type = ContentType.Article,
                title = "Core Promo 5",
                description = "Desc Core Promo 5"
            ),
        )
    )
)

sealed interface UiState {
    data object Loading: UiState
    data class DiscoverContent(
        val categories: ImmutableList<UICategory> = persistentListOf(),
        val featuredContent: ImmutableList<UIContent> = persistentListOf(),
        val promotedContent: ImmutableMap<UICategory, ImmutableList<UIContent>> = persistentMapOf(),
    ) : UiState

    data class Error(
        val error: ErrorState<DiscoverErrors>
    ) : UiState
}

data class UICategory(
    val id: String,
    val name: String,
)

data class UIContent(
    val id: String,
    val img: String,
    val type: ContentType,
    val title: String,
    val description: String,
)

enum class DiscoverErrors {
    GenericError,
    NetworkError,
}

sealed interface Intent {
    data object FetchFeatureItems : Intent
}
