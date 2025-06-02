plugins {
    alias(libs.plugins.kesiandroid.android.library)
    alias(libs.plugins.kesiandroid.hilt)
}

android {
    namespace = "com.kesicollection.test.instrumentation"
}

dependencies {
    api(projects.test.core)
    implementation(libs.androidx.test.runner)
    implementation(libs.hilt.android.testing)
}