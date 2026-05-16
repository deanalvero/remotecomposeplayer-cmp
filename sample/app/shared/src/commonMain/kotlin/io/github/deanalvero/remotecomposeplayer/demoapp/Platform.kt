package io.github.deanalvero.remotecomposeplayer.demoapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform