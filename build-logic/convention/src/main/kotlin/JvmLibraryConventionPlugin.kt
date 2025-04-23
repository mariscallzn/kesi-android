import com.kesicollection.buildlogic.configureKotlinJvm
import com.kesicollection.buildlogic.implementation
import com.kesicollection.buildlogic.libs
import com.kesicollection.buildlogic.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

/**
 * A convention plugin that applies common configurations for JVM Library projects.
 *
 * This plugin applies the following plugins:
 * - `org.jetbrains.kotlin.jvm`: Enables Kotlin JVM support.
 * - `org.jetbrains.kotlin.plugin.serialization`: Enables Kotlin serialization plugin.
 *
 * It also configures Kotlin JVM settings using [configureKotlinJvm] and adds common dependencies:
 * - `kotlinx.serialization.json`: Kotlin serialization JSON library.
 * - `kotlin.test`: Kotlin test library.
 * - `kotlinx.coroutines.test`: Kotlin coroutines test library.
 *
 * Usage:
 * Apply this plugin to your JVM library module's `build.gradle.kts` file:
 *
 * ```kotlin
 * plugins {
 *    id("your.plugin.id.jvm.library") // Replace with your plugin ID
 * }
 * ```
 */
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.jvm")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            configureKotlinJvm()
            dependencies {
                implementation(libs.findLibrary("kotlinx.serialization.json").get())
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                testImplementation(libs.findLibrary("kotlin.test").get())
                testImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
            }
        }
    }
}