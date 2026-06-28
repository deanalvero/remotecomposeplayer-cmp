package io.github.deanalvero.remotecomposeplayer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeEngine
import io.github.deanalvero.remotecomposeplayer.ui.RemoteComposeRenderer
import io.github.deanalvero.remotecomposeplayer.ui.buildRcTree

@Composable
fun RemoteComposePlayer(
    rcBytes: ByteArray,
    modifier: Modifier = Modifier
) {
    val operations = remember(rcBytes) {
        RemoteComposeEngine.parseStream(rcBytes)
    }
    val nodes = remember(operations) {
        listOf(buildRcTree(operations))
    }
    val isDark = isSystemInDarkTheme()
    val context = remember(operations, isDark) { RemoteComposeContext(operations, isDark) }
    context.Ticker()

    RemoteComposeRenderer(
        nodes = nodes,
        context = context,
        modifier = modifier
    )
}