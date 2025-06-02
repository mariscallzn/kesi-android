package com.kesicollection.test.instrumentation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * A custom [AndroidJUnitRunner] that uses Hilt for dependency injection in tests.
 *
 * This runner ensures that the [HiltTestApplication] is used, which is required for Hilt to
 * properly inject dependencies in test classes.
 */
class HiltTestRunner : AndroidJUnitRunner() {
    /**
     * Creates a new instance of the application class under test.
     *
     * This method is overridden to ensure that [HiltTestApplication] is used as the application
     * class.
     *
     * @param cl The ClassLoader to use for loading the application class.
     * @param className The name of the application class to instantiate. This is ignored and replaced with [HiltTestApplication].
     * @param context The context for the new application.
     * @return A new instance of [HiltTestApplication].
     */
    @SuppressLint("MissingSuperCall") // Suppressing this as we are intentionally replacing the className
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? =
        super.newApplication(
            cl,
            HiltTestApplication::class.java.name,
            context
        )
}