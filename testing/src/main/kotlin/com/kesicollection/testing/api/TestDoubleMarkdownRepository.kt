package com.kesicollection.testing.api

import com.kesicollection.data.repository.MarkdownRepository
import com.kesicollection.testing.testdata.ArticlesTestData

var downloadAsStringResult = Result.success(
    ArticlesTestData.items.first().markdown
)

class TestDoubleMarkdownRepository : MarkdownRepository {
    override suspend fun downloadAsString(fileName: String): Result<String> = downloadAsStringResult
}