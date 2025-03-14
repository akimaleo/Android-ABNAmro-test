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

rootProject.name = "ABN-Amro"
include(":app")
include(":feature-list")
include(":feature-detail")
include(":foundation-compose")
include(":foundation-kotlin")
include(":foundation-network")
include(":feature-list:domain")
include(":feature-list:data")
