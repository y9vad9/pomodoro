pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    plugins {
        kotlin("plugin.serialization") version "1.7.20"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.y9vad9.com")
    }
}

rootProject.name = "pomodoro"

includeBuild("build-logic/dependencies")
includeBuild("build-logic/configuration")

include(
    "backend:domain",
    "backend:endpoints",
    "backend:use-cases",
    ":backend:application",
    ":backend:adapters:repositories-integration"
)
