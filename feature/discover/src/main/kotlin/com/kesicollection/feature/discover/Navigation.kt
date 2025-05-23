package com.kesicollection.feature.discover

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kesicollection.core.app.AppRoute
import com.kesicollection.core.model.ContentType
import kotlinx.serialization.Serializable

/**
 * Represents the route for the Discover screen.
 * This object is used for navigation purposes within the app.
 */
@Serializable
data object DiscoverRoute : AppRoute

/**
 * Defines the Discover screen within the navigation graph.
 *
 * This function sets up the composable for the Discover screen, handling navigation
 * and interactions such as clicking on an article or a "See All" button.
 *
 * @param modifier The modifier to be applied to the Discover screen.
 * @param onArticleClick A lambda function to be invoked when an article is clicked.
 *                       It receives the ID of the clicked article as a [String].
 * @param onSeeAllClick A lambda function to be invoked when a "See All" button for a category is clicked.
 */
fun NavGraphBuilder.discover(
    modifier: Modifier = Modifier,
    onArticleClick: (articleId: String) -> Unit,
    onSeeAllClick: () -> Unit,
) {
    composable<DiscoverRoute> {
        // Remember the content click handler to avoid recomposition when the lambda doesn't change.
        // This handler determines the action based on the type of content clicked.
        val rememberedOnContentClick = remember<(UIContent) -> Unit> {
            {
                when (it.type) {
                    ContentType.Article -> onArticleClick(it.id)
                    ContentType.Podcast -> {}
                    ContentType.Video -> {}
                    ContentType.Demo -> {}
                }
            }
        }

        // Remember the "See All" click handler.
        // This is invoked when the user wants to view all content within a specific category.
        val rememberedOnSeeAllClick = remember<(UICategory) -> Unit> {
            {
                // For now we will simple show all the articles with no filter applied.
                // The specific category `it` is passed but currently not used to filter.
                onSeeAllClick()
            }
        }

        DiscoverScreen(
            modifier = modifier,
            onSeeAllClick = rememberedOnSeeAllClick,
            onContentClick = rememberedOnContentClick
        )
    }
}