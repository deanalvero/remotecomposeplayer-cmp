package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example

@Composable
fun ExamplesListScreen(
    examples: List<Example>,
    onExampleSelected: (Example) -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remote Compose Examples") },
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
        },
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(examples, key = { it.id }) { example ->
                ListItem(
                    headlineContent = { Text(example.title) },
                    supportingContent = { Text(example.subtitle) },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExampleSelected(example) }
                )
                HorizontalDivider()
            }
        }
    }
}