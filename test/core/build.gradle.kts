plugins {
    alias(libs.plugins.kesiandroid.jvm.library)
}

dependencies {
    api(projects.core.model)
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
    api(libs.kotlin.test)

    //The idea is to have both so I can show case the usage of both
    api(libs.io.mockk)
    //In the future if this module is migrated to KMP, these java libs won't be compatible
    api(libs.mockito.core)
    api(libs.mockito.kotlin)

    api(libs.truth)
}