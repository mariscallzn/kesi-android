package com.kesicollection.domain

import com.kesicollection.data.repository.BookmarkRepository
import javax.inject.Inject

/**
 * Use case for bookmarking an article by its ID.
 *
 * This use case encapsulates the logic for adding an article to the user's bookmarks.
 * It interacts with the [BookmarkRepository] to persist the bookmark operation.
 *
 * @property bookmarkRepository The repository responsible for managing bookmark data.
 */
class BookmarkArticleByIdUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(id: String) {
        bookmarkRepository.bookmarkArticleById(id)
    }
}