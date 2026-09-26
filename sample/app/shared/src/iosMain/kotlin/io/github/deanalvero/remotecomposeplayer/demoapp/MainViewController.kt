package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.ui.window.ComposeUIViewController
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.RemoteSourceScreen

fun MainViewController() = ComposeUIViewController {
    App(
        platformExamples = listOf(
            Example.PlatformExample(
                id = "remote-source",
                title = "Remote Source",
                subtitle = "Render Remote Compose from a remote source",
            ) {
                RemoteSourceScreen(
                    onBack = it
                )
            }
        )
    )
}