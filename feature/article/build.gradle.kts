plugins {
    alias(libs.plugins.kesiandroid.android.feature)
    alias(libs.plugins.kesiandroid.android.library.compose)
}

android {
    namespace = "com.kesicollection.feature.article"
}

dependencies {
    implementation(libs.coil.compose)
    testImplementation(libs.coil.test)
}