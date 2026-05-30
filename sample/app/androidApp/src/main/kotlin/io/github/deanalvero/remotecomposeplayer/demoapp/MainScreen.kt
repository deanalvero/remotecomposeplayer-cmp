package io.github.deanalvero.remotecomposeplayer.demoapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.remote.player.view.RemoteComposePlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.deanalvero.remotecomposeplayer.RemoteComposePlayer
import io.github.deanalvero.remotecomposeplayer.RemoteComposeVisualizer

@SuppressLint("RestrictedApi")
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is MainUiState.Loaded -> {
            val document = (uiState as MainUiState.Loaded).data
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
        is MainUiState.Loading -> {
            Text(text = "Loading...")
        }
        is MainUiState.Error -> {
            Text(text = "Error!")
        }
    }
}