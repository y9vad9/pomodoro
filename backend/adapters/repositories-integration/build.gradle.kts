plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(Deps.Libs.Exposed.Core)
    implementation(Deps.Libs.Exposed.Jdbc)
    implementation(project(Deps.Modules.Backend.Domain))
    implementation(project(Deps.Modules.Backend.UseCases))
    testImplementation(Deps.Libs.H2.Database)
    testImplementation(Deps.Libs.Slf4j.Simple)
    testImplementation(Deps.Libs.JUnit.Jupiter)
}

tasks.withType<Test> {
    useJUnitPlatform()
}