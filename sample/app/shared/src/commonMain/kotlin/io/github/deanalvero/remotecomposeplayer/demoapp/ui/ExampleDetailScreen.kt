package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.RemoteComposePlayer
import io.github.deanalvero.remotecomposeplayer.RemoteComposeVisualizer
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.ExampleCatalog

@Composable
fun ExampleDetailScreen(
    example: Example.Document,
    modifier: Modifier = Modifier,
    onDownload: (ByteArray) -> Unit,
    onBack: () -> Unit
) {
    val bytes = remember(example) { ExampleCatalog.bytesFor(example) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(example.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to examples"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onDownload(bytes) }) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = "Download ${example.id}.rc"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            val wide = maxWidth >= 800.dp

            if (wide) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LabeledPane(title = "Player", modifier = Modifier.weight(1f)) {
                        RemoteComposePlayer(
                            rcBytes = bytes,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    LabeledPane(title = "Operations", modifier = Modifier.weight(1f)) {
                        RemoteComposeVisualizer(
                            rcBytes = bytes,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            } else {
                var selectedTabIndex by remember { mutableStateOf(0) }

                Column(modifier = Modifier.fillMaxSize()) {
                    SecondaryTabRow(selectedTabIndex = selectedTabIndex) {
                        listOf("Player", "Operations").forEachIndexed { index, title ->
                            Tab(
                                selected = index == selectedTabIndex,
                                onClick = { selectedTabIndex = index }
                            ) {
                                Text(
                                    text = title,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> RemoteComposePlayer(
                            rcBytes = bytes,
                            modifier = Modifier.fillMaxSize()
                        )
                        1 -> RemoteComposeVisualizer(
                            rcBytes = bytes,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LabeledPane(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(modifier = modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                content()
            }
        }
    }
}