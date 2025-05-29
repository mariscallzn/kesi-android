package com.kesicollection.domain

import com.kesicollection.data.repository.MarkdownRepository
import javax.inject.Inject

/**
 * Use case for getting a markdown file as a string.
 *
 * @property markdownRepository The repository for accessing markdown files.
 */
class GetMarkdownAsString @Inject constructor(
    private val markdownRepository: MarkdownRepository,
) {
    /**
     * Invokes the use case to download a markdown file as a string.
     * @param fileName The name of the markdown file with extension to download.
     * @return A [Result] containing the markdown content as a string, or an error if the download fails.
     */
    suspend operator fun invoke(fileName: String): Result<String> =
        markdownRepository.downloadAsString(fileName)
}