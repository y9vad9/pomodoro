plugins {
    id(Deps.Plugins.Configuration.Kotlin.Mpp)
    id(Deps.Plugins.Android.Library)
}

kotlin {
    android()

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(Deps.Libs.Google.PlayServicesAuth)
            }
        }
    }
}

android {
    compileSdk = Deps.compileSdkVersion

    sourceSets {
        getByName("main") {
            manifest {
                srcFile("androidMain/AndroidManifest.xml")
            }
        }
    }
}

dependencies {
    commonMainApi(project(Deps.Modules.Client.Features.ViewModelMpp))
    commonMainApi(project(Deps.Modules.Sdk))
}