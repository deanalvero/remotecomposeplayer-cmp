package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundComponentKind
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundDocumentState
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundNode

@Composable
fun EditorSidebar(
    modifier: Modifier = Modifier,
    document: PlaygroundDocumentState,
    onAddRoot: (PlaygroundComponentKind) -> Unit,
    onSelectNode: (String?) -> Unit,
    onAddChild: (String, PlaygroundComponentKind) -> Unit,
    onDeleteNode: (String) -> Unit,
    onUpdateNode: (String, (PlaygroundNode) -> PlaygroundNode) -> Unit,
    onMoveNode: (nodeId: String, direction: Int) -> Unit,
    onDownload: () -> Unit,
    onUpload: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Editor tree",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onUpload) {
                Icon(Icons.Filled.Upload, contentDescription = "Upload .rc")
            }
            IconButton(onClick = onDownload) {
                Icon(Icons.Filled.Download, contentDescription = "Download .rc")
            }
        }

        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (document.nodes.isEmpty()) {
                Text(
                    text = "Empty playground. Add a component to start.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                document.nodes.forEachIndexed { index, node ->
                    NodeRow(
                        node = node,
                        depth = 0,
                        selectedId = document.selectedId,
                        onSelect = onSelectNode,
                        onAddChild = onAddChild,
                        onDelete = onDeleteNode,
                        onUpdateNode = onUpdateNode,
                        onMoveUp = if (index > 0)
                            ({ onMoveNode(node.id, -1) })
                        else null,
                        onMoveDown = if (index < document.nodes.lastIndex)
                            ({ onMoveNode(node.id, 1) })
                        else null,
                        onMoveNode = onMoveNode
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            ComponentAddMenu(
                buttonLabel = "Add component",
                onAdd = onAddRoot
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}