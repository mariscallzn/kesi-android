package com.kesicollection.core.model

/**
 * Represents an error state with a specific [type] and an optional [message].
 *
 * @param T The type of the enum representing the error type.
 * @property type The enum value representing the specific error type.
 * @property message An optional message providing more details about the error. Can be null if no extra information is available.
 */
data class ErrorState<T : Enum<T>>(
    val type: T,
    val message: String? = null
)