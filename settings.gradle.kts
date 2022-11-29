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
includeBuild("build-logic/service-deploy")

include(
    ":backend:domain",
    ":backend:use-cases",
    ":backend:application",
    ":backend:adapters:repositories-integration",
    ":backend:adapters:google-auth-integration",
    ":backend:adapters:time-integration",
    ":backend:adapters:tokens-integration",
    ":backend:adapters:codes-integration",
    ":sdk",
    ":client:common",
    ":client:features:mpp-viewmodel",
    ":client:android"
)
