package com.kesicollection.testing.api

import com.kesicollection.data.repository.BookmarkRepository

var isBookmarkedResult = false

class TestDoubleBookmarkRepository : BookmarkRepository {
    override suspend fun bookmarkArticleById(id: String) {
        /* no-op */
    }

    override suspend fun isBookmarked(id: String): Boolean = isBookmarkedResult
}