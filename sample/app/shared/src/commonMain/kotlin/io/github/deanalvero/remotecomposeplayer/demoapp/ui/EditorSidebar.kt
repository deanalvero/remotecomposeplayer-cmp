package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundComponentKind
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundDocumentState

@Composable
fun EditorSidebar(
    modifier: Modifier = Modifier,
    document: PlaygroundDocumentState,
    onAddRoot: (PlaygroundComponentKind) -> Unit,
    onSelectNode: (String?) -> Unit,
    onAddChild: (String, PlaygroundComponentKind) -> Unit,
    onDeleteNode: (String) -> Unit,
    onDownload: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxHeight().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onDownload) { Text("Download .rc") }
                    Button(onClick = { onSelectNode(null) }) { Text("Clear selection") }
                }
            }
        }

        Card {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Editor tree", style = MaterialTheme.typography.titleMedium)

                if (document.nodes.isEmpty()) {
                    Text("Empty playground. Add a component to start.", color = Color.Gray)
                    ComponentAddMenu(
                        buttonLabel = "Add component",
                        onAdd = onAddRoot
                    )
                } else {
                    document.nodes.forEach { node ->
                        NodeRow(
                            node = node,
                            depth = 0,
                            selectedId = document.selectedId,
                            onSelect = onSelectNode,
                            onAddChild = onAddChild,
                            onDelete = onDeleteNode
                        )
                    }
                }
            }
        }
    }
}