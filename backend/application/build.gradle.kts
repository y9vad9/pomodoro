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
    implementation("io.ktor:ktor-server-core-jvm:2.1.2")
    implementation("io.ktor:ktor-server-host-common-jvm:2.1.2")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.1.2")
}