package io.github.deanalvero.remotecomposeplayer.demoapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.remote.player.view.RemoteComposePlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.deanalvero.remotecomposeplayer.RemoteComposePlayer
import io.github.deanalvero.remotecomposeplayer.RemoteComposeOperationsList

@SuppressLint("RestrictedApi")
@Composable
fun RemoteSourceScreen(
    viewModel: RemoteSourceViewModel = viewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remote Source") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is RemoteSourceUiState.Loaded -> {
                    val document = (uiState as RemoteSourceUiState.Loaded).data
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
                        RemoteComposeOperationsList(
                            rcBytes = document,
                            modifier = Modifier.weight(0.50f)
                        )
                    }
                }

                is RemoteSourceUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is RemoteSourceUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiState as RemoteSourceUiState.Error).message
                                ?: "An unknown error occurred"
                        )
                        Text(
                            text = "Run the server using the ./gradlew :sample:server:run command.",
                            fontSize = 10.sp
                        )
                        Button(
                            onClick = { viewModel.load() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
        }
    }
}