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
    onDownload: (ByteArray) -> Unit = {}
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
            onDownload = onDownload,
            onBack = { selected = null }
        )

        is Example.Document -> ExampleDetailScreen(
            example = example,
            onBack = { selected = null },
            modifier = modifier
        )
    }
}