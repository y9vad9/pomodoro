plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Backend.UseCases))
    implementation(project(Deps.Modules.Backend.Domain))
}