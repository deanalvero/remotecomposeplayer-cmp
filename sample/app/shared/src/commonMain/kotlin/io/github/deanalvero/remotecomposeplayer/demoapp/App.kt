package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example

@Composable
@Preview
fun App(
    platformExamples: List<Example> = emptyList(),
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    MaterialTheme {
        ExamplesApp(
            platformExamples = platformExamples,
            onDownload = { filename, bytes ->
                downloadDocument(bytes, filename)
            },
            onNavHostReady = onNavHostReady
        )
    }
}