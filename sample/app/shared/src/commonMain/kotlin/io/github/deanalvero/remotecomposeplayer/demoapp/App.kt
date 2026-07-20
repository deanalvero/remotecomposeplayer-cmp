package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example

@Composable
@Preview
fun App(
    platformExamples: List<Example> = emptyList()
) {
    MaterialTheme {
        ExamplesApp(
            platformExamples = platformExamples,
            onDownload = { filename, bytes ->
                downloadDocument(bytes, filename)
            }
        )
    }
}