package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.MainScreen

@Composable
fun PlaygroundApp(
    modifier: Modifier = Modifier,
    onDownload: (ByteArray) -> Unit = {},
    onBack: (() -> Unit)? = null
) {
    val viewModel = remember { MainViewModel() }
    MainScreen(
        viewModel = viewModel,
        modifier = modifier,
        onDownload = onDownload,
        onBack = onBack
    )
}