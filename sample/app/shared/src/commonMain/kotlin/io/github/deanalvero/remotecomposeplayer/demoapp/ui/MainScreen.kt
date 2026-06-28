package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import io.github.deanalvero.remotecomposeplayer.demoapp.MainUiState
import io.github.deanalvero.remotecomposeplayer.demoapp.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onDownload: (ByteArray) -> Unit = {},
    onBack: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remote Compose Playground") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to examples"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            uriHandler.openUri("https://github.com/deanalvero/remotecomposeplayer-cmp")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "Open Source Code Repository",
                            tint = LocalContentColor.current
                        )
                    }
                }
            )
        }
    ) {
        Surface(modifier = modifier.padding(it).fillMaxSize()) {
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
                        onUpload = viewModel::importDocument,
                        onMoveNode = viewModel::moveNode,
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
}