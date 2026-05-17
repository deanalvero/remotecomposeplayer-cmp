package io.github.deanalvero.remotecomposeplayer.demoapp

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.remote.player.view.RemoteComposePlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("RestrictedApi")
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is MainUiState.Loaded -> {
            AndroidView(
                factory = { context ->
                    RemoteComposePlayer(context).also {
                        val document = (uiState as MainUiState.Loaded).data
                        it.setDocument(document)
                    }
                }
            )
        }
        is MainUiState.Loading -> {
            Text(text = "Loading...")
        }
        is MainUiState.Error -> {
            Text(text = "Error!")
        }
    }
}