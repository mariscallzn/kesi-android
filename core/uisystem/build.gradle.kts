plugins {
    alias(libs.plugins.kesiandroid.android.library)
    alias(libs.plugins.kesiandroid.android.library.compose)
}

android {
    namespace = "com.kesicollection.core.uisystem"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)
    api(libs.play.services.ads)
    api(projects.core.app)

    implementation(libs.coil.core)
    implementation(libs.markdown) {
        exclude(group = "org.jetbrains", module = "annotations-java5")
    }
    implementation(libs.syntax.highlight) {
        exclude(group = "org.jetbrains", module = "annotations-java5")
    }

    debugImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.test.rules)
}
