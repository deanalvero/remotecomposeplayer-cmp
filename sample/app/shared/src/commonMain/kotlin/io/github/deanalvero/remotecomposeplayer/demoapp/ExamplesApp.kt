package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.ExampleCatalog
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.ExampleDetailScreen
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.ExamplesListScreen

@Composable
fun ExamplesApp(
    modifier: Modifier = Modifier,
    onDownload: (filename: String, bytes: ByteArray) -> Unit = { _, _ -> }
) {
    var selected by remember { mutableStateOf<Example?>(null) }

    when (val example = selected) {
        null -> ExamplesListScreen(
            examples = ExampleCatalog.all,
            onExampleSelected = { selected = it },
            modifier = modifier
        )

        Example.Playground -> PlaygroundApp(
            modifier = modifier,
            onDownload = { onDownload("playground.rc", it) },
            onBack = { selected = null }
        )

        is Example.Document -> ExampleDetailScreen(
            example = example,
            modifier = modifier,
            onDownload = { onDownload("${example.id}.rc", it) },
            onBack = { selected = null }
        )
    }
}