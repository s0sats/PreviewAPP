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
        val localLib = File(rootDir, "../namoa-lib-local")
        if (localLib.exists()) {
            maven { url = uri(localLib) }
        }
        maven {
            url = uri("file:///C:/Android/LIB")
        }
    }
}

rootProject.name = "PRJ001"
include(":app")
