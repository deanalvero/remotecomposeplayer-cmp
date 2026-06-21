package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundNode

@Composable
fun EditorTreeNodeRow(
    node: PlaygroundNode,
    depth: Int,
    selectedId: String?,
    onSelectNode: (String?) -> Unit
) {
    val isSelected = node.id == selectedId

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${"  ".repeat(depth)}${nodeLabel(node)}  •  id=${node.id}  •  component=${node.componentId}",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectNode(node.id) }
                .padding(vertical = 8.dp, horizontal = 8.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
            style = MaterialTheme.typography.bodyMedium
        )

        when (node) {
            is PlaygroundNode.Column -> node.children.forEach { child ->
                EditorTreeNodeRow(
                    node = child,
                    depth = depth + 1,
                    selectedId = selectedId,
                    onSelectNode = onSelectNode
                )
            }

            is PlaygroundNode.Row -> node.children.forEach { child ->
                EditorTreeNodeRow(
                    node = child,
                    depth = depth + 1,
                    selectedId = selectedId,
                    onSelectNode = onSelectNode
                )
            }

            is PlaygroundNode.Box -> node.children.forEach { child ->
                EditorTreeNodeRow(
                    node = child,
                    depth = depth + 1,
                    selectedId = selectedId,
                    onSelectNode = onSelectNode
                )
            }

            is PlaygroundNode.Canvas -> Unit
            is PlaygroundNode.Text -> Unit
            is PlaygroundNode.Spacer -> Unit
        }
    }
}
