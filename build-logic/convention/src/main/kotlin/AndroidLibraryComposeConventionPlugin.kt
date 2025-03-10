import com.android.build.gradle.LibraryExtension
import com.kesicollection.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

/**
 * A convention plugin that applies common configurations for Android Library projects using Compose.
 *
 * This plugin applies the following plugins:
 * - `com.android.library`: Enables Android library capabilities.
 * - `org.jetbrains.kotlin.plugin.compose`: Enables Kotlin Compose compiler plugin.
 *
 * It also configures the Android Library extension with common Compose settings using [configureAndroidCompose].
 *
 * Usage:
 * Apply this plugin to your Android library module's `build.gradle.kts` file:
 *
 * ```kotlin
 * plugins {
 *      id("your.plugin.id.android.library.compose") // Replace with your plugin ID
 * }
 * ```
 *
 */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}