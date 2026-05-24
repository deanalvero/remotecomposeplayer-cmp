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

    implementation("androidx.compose.remote:remote-core:1.0.0-alpha11")

//    implementation("androidx.compose.remote:remote-creation:1.0.0-alpha11")
//    implementation("androidx.compose.remote:remote-creation-core:1.0.0-alpha11")
    implementation("androidx.compose.remote:remote-creation-jvm:1.0.0-alpha11")
}