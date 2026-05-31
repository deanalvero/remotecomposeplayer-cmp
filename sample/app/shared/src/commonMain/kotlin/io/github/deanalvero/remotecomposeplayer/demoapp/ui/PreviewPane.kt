package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.RemoteComposePlayer
import io.github.deanalvero.remotecomposeplayer.RemoteComposeVisualizer

@Composable
fun PreviewPane(
    bytes: ByteArray,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxHeight()) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Preview", style = MaterialTheme.typography.titleMedium)
            RemoteComposePlayer(
                rcBytes = bytes,
                modifier = Modifier.weight(0.45f).fillMaxWidth()
            )
            Text("Operations", style = MaterialTheme.typography.titleMedium)
            RemoteComposeVisualizer(
                rcBytes = bytes,
                modifier = Modifier.weight(0.55f).fillMaxWidth()
            )
        }
    }
}