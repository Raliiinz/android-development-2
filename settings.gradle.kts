pluginManagement {
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

rootProject.name = "AndroidDevelopment2"
include(":app")
include(":auth")
include(":core:base")
include(":core:data")
include(":core:domain")
include(":core:network")
include(":core:utils")
include(":feature:search")
include(":feature:recipe-details")
include(":core:navigation")
