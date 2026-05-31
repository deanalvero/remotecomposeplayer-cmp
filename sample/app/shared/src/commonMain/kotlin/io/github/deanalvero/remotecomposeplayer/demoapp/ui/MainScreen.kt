package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.demoapp.MainUiState
import io.github.deanalvero.remotecomposeplayer.demoapp.MainViewModel
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onDownload: (ByteArray) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is MainUiState.Playground -> {
                PlaygroundEditor(
                    document = state.document,
                    onAddRoot = viewModel::addRoot,
                    onSelectNode = viewModel::selectNode,
                    onAddChild = viewModel::addChild,
                    onUpdateNode = viewModel::updateNode,
                    onDeleteNode = viewModel::deleteNode,
                    onDownload = onDownload,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is MainUiState.Error -> {
                ErrorPane(
                    message = state.message,
                    onReset = viewModel::reload,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
