plugins {
    alias(libs.plugins.kesiandroid.android.feature)
    alias(libs.plugins.kesiandroid.android.library.compose)
}

android {
    namespace = "com.kesicollection.feature.article"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.core)
    implementation(libs.coil.network.okhttp)
    implementation(libs.markdown)
    testImplementation(libs.coil.test)
}