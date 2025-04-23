package com.kesicollection.data.api

/**
 * Interface defining the contract for bookmarking articles.
 *
 * This interface provides methods to manage the bookmarking status of articles,
 * including adding and checking bookmarks.
 */
interface BookmarkApi {
    /**
     * Bookmarks or unbookmarks an article by its unique identifier.
     *
     * This function asynchronously toggles the bookmark status of the article with the specified ID.
     * If the article is not bookmarked, it will be bookmarked. If it is already bookmarked, it will be unbookmarked.
     * The actual implementation of how the bookmark status is stored (e.g., in a database, shared preferences, etc.)
     * is hidden within this function.
     *
     * This is a suspending function, meaning it must be called within a coroutine or another suspending function.
     *
     * @param id The unique identifier of the article to bookmark or unbookmark.
     * @throws Exception If an error occurs during the bookmarking/unbookmarking process, such as network issues or database errors.
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