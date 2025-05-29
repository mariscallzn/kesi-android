/**
 * This file contains the implementation of the MarkdownApi interface using OkHttp.
 * For simplicity this class is in tha app module, but it should have it's own :network module
 * but right not it's an overkill since there is only one controller.
 */

package com.kesicollection.kesiandroid.network

import com.kesicollection.core.app.qualifiers.KesiAndroidApiUrl
import com.kesicollection.data.api.MarkdownApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [MarkdownApi] that uses [OkHttpClient] to download markdown files.
 * This class is a Singleton, meaning only one instance of it will be created.
 *
 * @property okHttpClient The [OkHttpClient] instance used for making network requests.
 * @property kesiAndroidApiUrl The base URL for the Kesi Android API, injected with the [KesiAndroidApiUrl] qualifier.
 */
@Singleton
class OkHttpMarkdownApi @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @KesiAndroidApiUrl
    private val kesiAndroidApiUrl: String,
) : MarkdownApi {
    /**
     * Downloads a markdown file as a String.
     *
     * This function is a suspend function, meaning it can be paused and resumed, allowing for non-blocking network operations.
     * It uses [Dispatchers.IO] to ensure the network request is performed on a background thread.
     *
     * @param fileName The name of the markdown file to download (e.g., "about_us.md").
     * @return A [Result] object containing the downloaded String on success, or an exception on failure.
     */
    override suspend fun downloadAsString(fileName: String): Result<String> = Result.runCatching {
        //TODO: Extract the dispatcher.
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("${kesiAndroidApiUrl}markdown/$fileName")
                .build()
            okHttpClient.newCall(request).execute().use { response ->
                response.body?.string() ?: "TODO: SHOW GENERIC MD ERROR"
            }
        }
    }
}