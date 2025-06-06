pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KesiAndroid"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:uisystem")
include(":core:model")
include(":data:api")
include(":data:retrofit")
include(":feature:articles")
include(":feature:article")
include(":feature:audioplayer")
include(":data:repository")
include(":data:datastore")
include(":core:app")
include(":feature:discover")
include(":domain")
include(":test:core")
include(":test:instrumentation")
