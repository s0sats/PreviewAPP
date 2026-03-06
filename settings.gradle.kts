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
        maven { url = uri("https://jitpack.io") }
        val ciRepo = System.getenv("CI_MAVEN_REPO")
        if (ciRepo != null) {
            maven { url = uri(ciRepo) }
        }
        maven {
            url = uri("file:///C:/Android/LIB")
        }
    }
}

rootProject.name = "PRJ001"
include(":app")
