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
include(":feature:home")
include(":feature:quiz")
include(":feature:quiztopics")
include(":data:retrofit")
include(":data:room")
include(":feature:doggallery")
include(":feature:articles")
include(":testing")
include(":feature:article")
include(":feature:audioplayer")
include(":data:repository")
include(":data:usecase")
