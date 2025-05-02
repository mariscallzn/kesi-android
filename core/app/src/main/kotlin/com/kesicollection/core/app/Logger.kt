package com.kesicollection.core.app

/**
 * Interface for logging messages and errors.
 *
 * This interface provides a standardized way to log information and errors within the application.
 * Implementations of this interface can handle logging to various destinations like the console,
 * files, or remote logging services.
 */
interface Logger {
    fun log(tag: String, message: String)
    fun error(tag: String, message: String, exception: Exception? = null)
}