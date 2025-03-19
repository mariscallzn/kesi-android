plugins {
    alias(libs.plugins.kesiandroid.android.library)
    alias(libs.plugins.kesiandroid.hilt)
}

android {
    namespace = "com.kesicollection.data.retrofit"
}

dependencies {
    api(projects.data.api)
}