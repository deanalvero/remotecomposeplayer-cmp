package io.github.deanalvero.remotecomposeplayer.demoapp

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}