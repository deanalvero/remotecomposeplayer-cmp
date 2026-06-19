package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.draw.DrawOperationSection
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier.ModifierChip
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier.ModifierEditDialog
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier.ModifierPickerDialog
import io.github.deanalvero.remotecomposeplayer.operation.common.RcDimensionType
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundComponentKind
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundModifier
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundNode
import io.github.deanalvero.remotecomposeplayer.playground.defaultDrawOperation
import io.github.deanalvero.remotecomposeplayer.playground.defaultModifier
import kotlin.math.abs
import kotlin.math.roundToInt

private val ColumnAccent = Color(0xFF10B981)
private val RowAccent = Color(0xFF3B82F6)
private val CanvasAccent = Color(0xFF8B5CF6)
private val TextAccent = Color(0xFFD97706)

private const val DpPerStep = 60f

@Composable
fun NodeRow(
    node: PlaygroundNode,
    depth: Int,
    selectedId: String?,
    onSelect: (String?) -> Unit,
    onAddChild: (String, PlaygroundComponentKind) -> Unit,
    onDelete: (String) -> Unit,
    onUpdateNode: (String, (PlaygroundNode) -> PlaygroundNode) -> Unit,
    onMoveUp: (() -> Unit)? = null,
    onMoveDown: (() -> Unit)? = null,
    onMoveNode: ((nodeId: String, direction: Int) -> Unit)? = null
) {
    val isContainerWithChildren = node is PlaygroundNode.Column || node is PlaygroundNode.Row
    val isCanvas = node is PlaygroundNode.Canvas
    val children: List<PlaygroundNode> = when (node) {
        is PlaygroundNode.Column -> node.children
        is PlaygroundNode.Row -> node.children
        else -> emptyList()
    }
    val selected = node.id == selectedId

    val accentColor = when (node) {
        is PlaygroundNode.Column -> ColumnAccent
        is PlaygroundNode.Row -> RowAccent
        is PlaygroundNode.Canvas -> CanvasAccent
        is PlaygroundNode.Text -> TextAccent
    }

    var expanded by remember(node.id) { mutableStateOf(true) }
    var isDragging by remember(node.id) { mutableStateOf(false) }
    var dragOffsetY by remember(node.id) { mutableFloatStateOf(0f) }
    var editingModifierIndex by remember { mutableStateOf<Int?>(null) }
    var showModifierPicker by remember { mutableStateOf(false) }

    editingModifierIndex?.let { idx ->
        node.modifiers.getOrNull(idx)?.let { mod ->
            ModifierEditDialog(
                modifier = mod,
                onDismiss = { editingModifierIndex = null },
                onChange = { updated ->
                    onUpdateNode(node.id) { n -> n.replaceModifier(idx, updated) }
                },
                onDelete = {
                    onUpdateNode(node.id) { n -> n.removeModifier(idx) }
                    editingModifierIndex = null
                }
            )
        }
    }

    if (showModifierPicker) {
        ModifierPickerDialog(
            onDismiss = { showModifierPicker = false },
            onPick = { kind ->
                val newIndex = node.modifiers.size
                showModifierPicker = false
                onUpdateNode(node.id) { n -> n.addModifier(defaultModifier(kind)) }
                editingModifierIndex = newIndex
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (depth * 16).dp)
            .zIndex(if (isDragging) 1f else 0f)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selected)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                else
                    MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isDragging) 8.dp else 1.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .fillMaxHeight()
                            .background(accentColor)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(node.id) }
                                .padding(start = 8.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DragIndicator,
                                contentDescription = "Reorder",
                                tint = if (isDragging) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                                },
                                modifier = Modifier
                                    .size(14.dp)
                                    .pointerInput(node.id) {
                                        detectDragGesturesAfterLongPress(
                                            onDragStart = {
                                                isDragging = true
                                                dragOffsetY = 0f
                                            },
                                            onDrag = { _, delta -> dragOffsetY += delta.y },
                                            onDragEnd = {
                                                isDragging = false
                                                commitDrag(
                                                    dragOffsetY = dragOffsetY,
                                                    density = density,
                                                    onMoveUp = onMoveUp,
                                                    onMoveDown = onMoveDown,
                                                )
                                                dragOffsetY = 0f
                                            },
                                            onDragCancel = {
                                                isDragging = false
                                                dragOffsetY = 0f
                                            }
                                        )
                                    }
                            )

                            Spacer(Modifier.width(6.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = nodeLabel(node),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "component #${node.componentId}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                                )
                            }

                            SmallIconButton(
                                enabled = onMoveUp != null,
                                onClick = { onMoveUp?.invoke() },
                                size = 14
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Move up",
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            SmallIconButton(
                                enabled = onMoveDown != null,
                                onClick = { onMoveDown?.invoke() },
                                size = 14
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Move down",
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            SmallIconButton(
                                onClick = { onDelete(node.id) },
                                size = 28,
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete ${nodeLabel(node)}",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                node.modifiers.forEachIndexed { index, mod ->
                                    ModifierChip(
                                        modifier = mod,
                                        onClick = { editingModifierIndex = index },
                                        onRemove = {
                                            onUpdateNode(node.id) { n -> n.removeModifier(index) }
                                        }
                                    )
                                }

                                Surface(
                                    onClick = { showModifierPicker = true },
                                    shape = RoundedCornerShape(999.dp),
                                    color = Color.Transparent,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "Add modifier",
                                            modifier = Modifier.size(10.dp),
                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                        Text(
                                            text = "Add modifier",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            if (isContainerWithChildren) {
                                ComponentAddMenu(
                                    buttonLabel = "Add child",
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    onAdd = { kind ->
                                        onAddChild(node.id, kind)
                                    }
                                )
                                Spacer(Modifier.height(6.dp))
                            }

                            if (isCanvas) {
                                DrawOperationSection(
                                    operations = node.drawOperations,
                                    onAddOperation = { kind ->
                                        onUpdateNode(node.id) { n ->
                                            val canvas = n as PlaygroundNode.Canvas
                                            canvas.copy(drawOperations = canvas.drawOperations + defaultDrawOperation(kind))
                                        }
                                    },
                                    onUpdateOperation = { index, updated ->
                                        onUpdateNode(node.id) { n ->
                                            val canvas = n as PlaygroundNode.Canvas
                                            canvas.copy(
                                                drawOperations = canvas.drawOperations.toMutableList().also {
                                                    it[index] = updated
                                                }
                                            )
                                        }
                                    },
                                    onDeleteOperation = { index ->
                                        onUpdateNode(node.id) { n ->
                                            val canvas = n as PlaygroundNode.Canvas
                                            canvas.copy(
                                                drawOperations = canvas.drawOperations.toMutableList().also {
                                                    it.removeAt(index)
                                                }
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                if (isContainerWithChildren && expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        children.forEachIndexed { childIndex, child ->
                            NodeRow(
                                node = child,
                                depth = depth + 1,
                                selectedId = selectedId,
                                onSelect = onSelect,
                                onAddChild = onAddChild,
                                onDelete = onDelete,
                                onUpdateNode = onUpdateNode,
                                onMoveUp = if (childIndex > 0)
                                    ({ onMoveNode?.invoke(child.id, -1) })
                                else null,
                                onMoveDown = if (childIndex < children.lastIndex)
                                    ({ onMoveNode?.invoke(child.id, 1) })
                                else null,
                                onMoveNode = onMoveNode,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun commitDrag(
    dragOffsetY: Float,
    density: Float,
    onMoveUp: (() -> Unit)?,
    onMoveDown: (() -> Unit)?
) {
    val pixelsPerStep = DpPerStep * density
    val steps = (dragOffsetY / pixelsPerStep).roundToInt()
    if (steps == 0) return

    val move: () -> Unit
    val count: Int
    if (steps > 0) {
        move = onMoveDown ?: return
        count = steps
    } else {
        move = onMoveUp ?: return
        count = abs(steps)
    }
    repeat(count.coerceAtMost(50)) { move() }
}

@Composable
private fun SmallIconButton(
    onClick: () -> Unit,
    size: Int,
    enabled: Boolean = true,
    tint: Color = Color.Unspecified,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        val resolvedTint = when {
            !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            tint != Color.Unspecified -> tint
            else -> Color.Unspecified
        }
        if (resolvedTint != Color.Unspecified) {
            CompositionLocalProvider(LocalContentColor provides resolvedTint) { content() }
        } else {
            content()
        }
    }
}

fun PlaygroundModifier.chipLabel(): String = when (this) {
    is PlaygroundModifier.Padding -> {
        val l = left.fmt()
        val t = top.fmt()
        val r = right.fmt()
        val b = bottom.fmt()
        when {
            l == t && t == r && r == b -> "pad $l"
            l == r && t == b -> "pad $l·$t"
            else -> "pad $l,$t,$r,$b"
        }
    }
    is PlaygroundModifier.Background -> {
        val hex = argb.toUInt().toString(16).uppercase().padStart(8, '0').takeLast(6)
        "bg #$hex"
    }
    is PlaygroundModifier.Width -> {
        val type = when (typeId) {
            RcDimensionType.FILL.id -> "fill"
            RcDimensionType.FILL_PARENT_MAX_WIDTH.id -> "fillMax"
            RcDimensionType.WRAP.id -> "wrap"
            RcDimensionType.WEIGHT.id -> "wt ${value.fmt()}"
            RcDimensionType.EXACT.id,
            RcDimensionType.EXACT_DP.id -> "${value.fmt()}dp"
            else -> "w:$typeId"
        }
        "w $type"
    }
}

private fun Float.fmt(): String =
    if (this == toLong().toFloat()) toLong().toString() else toString()
