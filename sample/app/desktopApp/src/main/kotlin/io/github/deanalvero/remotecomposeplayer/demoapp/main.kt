package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RemoteComposePlayerCMP",
    ) {
        App()
    }
}