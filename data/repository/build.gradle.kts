plugins {
    alias(libs.plugins.kesiandroid.jvm.library)
    alias(libs.plugins.kesiandroid.hilt)
}

dependencies {
    api(projects.core.model)
    implementation(projects.data.api)
}