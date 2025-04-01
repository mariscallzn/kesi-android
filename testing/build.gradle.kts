plugins {
    alias(libs.plugins.kesiandroid.android.library)
    alias(libs.plugins.kesiandroid.hilt)
}

android {
    namespace = "com.kesicollection.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(projects.core.model)

    implementation(libs.hilt.android.testing)
    implementation(libs.androidx.test.rules)
}
