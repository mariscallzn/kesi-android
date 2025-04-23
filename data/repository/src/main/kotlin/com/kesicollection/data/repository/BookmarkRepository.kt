package com.kesicollection.data.repository

import com.kesicollection.data.api.BookmarkApi
import javax.inject.Inject

/**
 * Interface for managing bookmarked articles.
 *
 * This interface defines the contract for interacting with a data source
 * responsible for storing and retrieving information about bookmarked articles.
 */
interface BookmarkRepository {
    /**
     * Bookmarks or unbookmarks an article by its unique identifier.
     *
     * This function asynchronously toggles the bookmark status of an article with the specified ID.
     * If the article is not currently bookmarked, it will be bookmarked. If it is already bookmarked,
     * it will be unbookmarked (removed from bookmarks).
     * The underlying storage mechanism for bookmarks (e.g., database, shared preferences, etc.)
     * is abstracted away within this function.
     *
     * **Behavior:**
     *  - If the article with the given `id` is already bookmarked, it will be unbookmarked.
     *  - If the article with the given `id` is not bookmarked, it will be bookmarked.
     *
     * @param id The unique identifier of the article to bookmark or unbookmark. Must be a non-empty string.
     * @throws IllegalArgumentException If the provided `id` is empty.
     * @throws Exception If any other error occurs during the bookmarking or unbookmarking process.
     */
    suspend fun bookmarkArticleById(id: String)

    /**
     * Checks if a specific item, identified by its [id], is bookmarked.
     *
     * This function asynchronously queries the underlying data source (e.g., database, shared preferences)
     * to determine if an item with the given [id] has been previously marked as a bookmark.
     *
     * @param id The unique identifier of the item to check for bookmark status.
     * @return `true` if the item with the given [id] is bookmarked, `false` otherwise.
     * @throws Exception If any error occurs during the bookmark status retrieval process.
     */
    suspend fun isBookmarked(id: String): Boolean
}

/**
 * Implementation of the [BookmarkRepository] interface.
 * This class handles the interaction with the [BookmarkApi] to perform bookmark-related operations.
 *
 * @property bookmarkApi The [BookmarkApi] instance used for network calls to the bookmark service.
 */
class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkApi: BookmarkApi,
) : BookmarkRepository {
    override suspend fun bookmarkArticleById(id: String) {
        bookmarkApi.bookmarkArticleById(id)
    }

    override suspend fun isBookmarked(id: String): Boolean = bookmarkApi.isBookmarked(id)
}