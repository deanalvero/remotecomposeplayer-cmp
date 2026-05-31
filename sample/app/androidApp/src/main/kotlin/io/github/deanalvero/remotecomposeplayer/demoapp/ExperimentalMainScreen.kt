package io.github.deanalvero.remotecomposeplayer.demoapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.remote.player.core.RemoteDocument
import androidx.compose.remote.player.view.RemoteComposePlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.deanalvero.remotecomposeplayer.RemoteComposePlayer
import io.github.deanalvero.remotecomposeplayer.RemoteComposeVisualizer

@SuppressLint("RestrictedApi")
@Composable
fun ExperimentalMainScreen(
    viewModel: ExperimentalMainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is ExperimentalMainUiState.Loaded -> {
            val document = (uiState as ExperimentalMainUiState.Loaded).data
            val remoteDocument = remember(document) {
                RemoteDocument(document)
            }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AndroidView(
                    factory = { context ->
                        RemoteComposePlayer(context).also {
                            it.setDocument(document)
                        }
                    },
                    modifier = Modifier.weight(0.25f)
                )
                RemoteComposePlayer(
                    rcBytes = document,
                    modifier = Modifier.weight(0.25f)
                )
                RemoteComposeVisualizer(
                    rcBytes = document,
                    modifier = Modifier.weight(0.50f)
                )
            }
        }
        is ExperimentalMainUiState.Loading -> {
            Text(text = "Loading...")
        }
        is ExperimentalMainUiState.Error -> {
            Text(text = "Error!")
        }
    }
}