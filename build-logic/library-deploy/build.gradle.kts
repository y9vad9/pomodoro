plugins {
    `kotlin-dsl`
    id("dependencies")
}

group = "sdk-deploy"
version = "SNAPSHOT"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    plugins.register("sdk-deploy") {
        id = "sdk-deploy"
        implementationClass = "DeployPlugin"
    }
}

dependencies {
    implementation(Deps.Plugins.Dependencies.Classpath)
    implementation(Deps.Plugins.Ssh.Classpath)
    implementation(Deps.Plugins.Shadow.Classpath)
}