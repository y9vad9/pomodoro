import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Deps.Plugins.Configuration.Kotlin.Mpp)
    id(Deps.Plugins.Serialization.Id)
}

kotlin {
    js {
        browser()
    }
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Libs.Ktor.Client.Core)
                implementation(Deps.Libs.Ktor.Client.ContentNegotiation)
                implementation(Deps.Libs.Kotlinx.Serialization)
                implementation(Deps.Libs.Ktor.Json)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(project(Deps.Modules.Backend.Application))
                implementation(Deps.Libs.JUnit.Jupiter)
                implementation(Deps.Libs.Exposed.Core)
                implementation(Deps.Libs.H2.Database)
                implementation(Deps.Libs.Ktor.Server.Core)
                implementation(Deps.Libs.Slf4j.Simple)
                implementation(project(Deps.Modules.Backend.Adapters.GoogleAuthIntegration))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(Deps.Libs.Ktor.Client.Js)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(Deps.Libs.Ktor.Client.Cio)
            }
        }
    }

    explicitApi()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        languageVersion = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}