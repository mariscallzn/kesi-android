import com.android.build.gradle.api.AndroidBasePlugin
import com.kesicollection.buildlogic.implementation
import com.kesicollection.buildlogic.ksp
import com.kesicollection.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin that defines the Hilt base configuration used by Android and Kotlin.
 *
 * These plugins are applied:
 * - com.google.devtools.ksp
 * - dagger.hilt.android.plugin
 *
 * Add support for Jvm Module, base on org.jetbrains.kotlin.jvm and Add support for Android modules,
 * based on [AndroidBasePlugin]
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")

            dependencies {
                ksp(libs.findLibrary("hilt.compiler").get())
            }

            pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
                dependencies {
                    implementation(libs.findLibrary("hilt.core").get())
                }
            }

            pluginManager.withPlugin("com.android.base") {
                apply(plugin = "dagger.hilt.android.plugin")
                dependencies {
                    implementation(libs.findLibrary("hilt.android").get())
                }
            }
        }
    }
}