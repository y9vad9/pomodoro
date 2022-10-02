plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Backend.Domain))
    implementation(Deps.Libs.Kotlinx.Coroutines)
}