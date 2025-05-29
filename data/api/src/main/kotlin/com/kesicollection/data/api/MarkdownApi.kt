package com.kesicollection.data.api

/**
 * An API for downloading Markdown content.
 */
interface MarkdownApi {
    /**
     * Downloads the content of a markdown file as a String.
     *
     * @param fileName The identifier of the markdown file to download.
     * @return A [Result] object containing the downloaded String content if successful,
     *         or an error if the download fails.
     */
    suspend fun downloadAsString(fileName: String): Result<String>
}