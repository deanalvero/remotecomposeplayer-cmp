package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.ui.window.ComposeUIViewController
import io.github.deanalvero.remotecomposeplayer.RemoteComposePlayer

fun MainViewController() = ComposeUIViewController { RemoteComposePlayer() }