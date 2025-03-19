plugins {
    alias(libs.plugins.kesiandroid.android.library)
    alias(libs.plugins.kesiandroid.hilt)
    alias(libs.plugins.kesiandroid.android.room)
}

android {
    namespace = "com.kesicollection.data.room"
}

dependencies {
    api(projects.data.api)
}