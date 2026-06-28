package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        ExamplesApp(
            onDownload = { filename, bytes ->
                downloadDocument(bytes, filename)
            }
        )
    }
}