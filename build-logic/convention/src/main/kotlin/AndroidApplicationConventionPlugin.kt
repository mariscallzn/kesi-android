import com.android.build.api.dsl.ApplicationExtension
import com.kesicollection.buildlogic.TARGET_SDK
import com.kesicollection.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for Android application projects.
 *
 * This plugin applies necessary configurations for an Android application module.
 *
 * It applies the following plugins:
 * - `com.android.application`
 * - `org.jetbrains.kotlin.android`
 */
class AndroidApplicationConventionPlugin: Plugin<Project> {
    /**
     * Applies the plugin to the given project.
     *
     * @param target The project to apply the plugin to.
     */
    override fun apply(target: Project) {
        with(target) { // Apply plugins and configure the Android application extension.
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = TARGET_SDK
            }
        }
    }
}