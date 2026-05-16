plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
}

group = "io.github.deanalvero.remotecomposeplayer.demoapp"
version = "1.0.0"
application {
    mainClass = "io.github.deanalvero.remotecomposeplayer.demoapp.ApplicationKt"
}

dependencies {
    api(projects.sample.core)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}