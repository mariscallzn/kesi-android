package com.kesicollection.data.usecase

import com.kesicollection.data.repository.BookmarkRepository
import javax.inject.Inject

/**
 *  UseCase responsible for checking if an article is bookmarked.
 *
 *  This class provides a single public function, [invoke], which allows checking if an article
 *  with a specific ID is currently bookmarked. It leverages a [BookmarkRepository] to interact
 *  with the data layer.
 *
 *  @property bookmarkRepository The repository responsible for handling bookmark data.
 */
class IsArticleBookmarkedUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(id: String): Boolean = bookmarkRepository.isBookmarked(id)
}