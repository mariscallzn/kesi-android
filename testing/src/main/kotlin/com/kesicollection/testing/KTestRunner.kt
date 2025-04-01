package com.kesicollection.testing

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * A custom test runner that uses [HiltTestApplication] as the application class for instrumentation tests.
 *
 * This runner overrides the [newApplication] method to ensure that the Hilt testing application is used
 * instead of the default application class specified in the manifest. This is necessary for dependency
 * injection with Hilt to work correctly in instrumentation tests.
 */
class KTestRunner : AndroidJUnitRunner() {
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