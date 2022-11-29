plugins {
    id(Deps.Plugins.Kotlin.Android)
    id(Deps.Plugins.Android.Application)
}

android {
    compileSdk = Deps.compileSdkVersion

    defaultConfig {
        applicationId = "com.y9vad9.pomodoro.client.android"
        minSdk = Deps.minSdkVersion
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    implementation(Deps.Libs.Androidx.AppCompat)
    implementation(project(Deps.Modules.Client.Common))
    implementation(Deps.Libs.Androidx.Compose.UI)
    implementation(Deps.Libs.Androidx.Compose.Icons)
    implementation(Deps.Libs.Androidx.Compose.ExtendedIcons)
    implementation(Deps.Libs.Androidx.Compose.Foundation)
    implementation(Deps.Libs.Androidx.Compose.JUnitTests)
    implementation(Deps.Libs.Androidx.Compose.Material3)
}