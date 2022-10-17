plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
    id(Deps.Plugins.Serialization.Id)
}

dependencies {
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(Deps.Libs.Ktor.Server.Cio)
    implementation(Deps.Libs.Ktor.Server.Openapi)
    implementation(Deps.Libs.Ktor.Server.RequestValidation)
    implementation(Deps.Libs.Ktor.Server.ContentNegotiation)
    implementation(Deps.Libs.Ktor.Json)
    implementation(Deps.Libs.Kotlinx.Serialization)
    implementation(Deps.Libs.Kotlinx.Coroutines)
    implementation(project(Deps.Modules.Backend.UseCases))
    implementation(project(Deps.Modules.Backend.Domain))
    implementation(project(Deps.Modules.Backend.Adapters.RepositoriesIntegration))
    implementation(project(Deps.Modules.Backend.Adapters.TimeIntegration))
    implementation(project(Deps.Modules.Backend.Adapters.TokensIntegration))
    implementation(project(Deps.Modules.Backend.Adapters.GoogleAuthIntegration))
    implementation(project(Deps.Modules.Backend.Adapters.CodesIntegration))
    implementation(Deps.Libs.Exposed.Core)
    implementation(Deps.Libs.Exposed.Jdbc)
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(Deps.Libs.Ktor.Server.HostCommonJvm)
    implementation(Deps.Libs.Ktor.Server.StatusPages)
}