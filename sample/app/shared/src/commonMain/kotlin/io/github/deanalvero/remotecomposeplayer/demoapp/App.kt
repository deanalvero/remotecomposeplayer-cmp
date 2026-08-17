package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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
    var isDarkTheme by remember { mutableStateOf(false) }
    val colorScheme = if (isDarkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(colorScheme = colorScheme) {
        ExamplesApp(
            platformExamples = platformExamples,
            isDarkTheme = isDarkTheme,
            onToggleTheme = { isDarkTheme = !isDarkTheme },
            onDownload = { filename, bytes ->
                downloadDocument(bytes, filename)
            },
            onNavHostReady = onNavHostReady
        )
    }
}