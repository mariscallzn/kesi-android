plugins {
    alias(libs.plugins.kesiandroid.android.feature)
    alias(libs.plugins.kesiandroid.android.library.compose)
}

android {
    namespace = "com.kesicollection.feature.doggallery"
}

dependencies {
    implementation(libs.coil.compose)
}