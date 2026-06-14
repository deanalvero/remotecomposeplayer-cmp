
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier.ModifierEditDialog
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier.ModifierPickerDialog
import io.github.deanalvero.remotecomposeplayer.operation.common.RcDimensionType
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundComponentKind
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundModifier
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundNode
import io.github.deanalvero.remotecomposeplayer.playground.defaultModifier
import kotlin.math.abs
import kotlin.math.roundToInt

private val ColumnAccent = Color(0xFF10B981)
private val ColumnBadgeBg = Color(0xFFD1FAE5)
private val ColumnBadgeFg = Color(0xFF065F46)
private val RowAccent = Color(0xFF3B82F6)
private val RowBadgeBg = Color(0xFFDBEAFE)
private val RowBadgeFg = Color(0xFF1E40AF)
private val TextAccent = Color(0xFFD97706)
private val TextBadgeBg = Color(0xFFFEF3C7)
private val TextBadgeFg = Color(0xFF92400E)
private val PaddingChipBg = Color(0xFFFFF7ED)
private val PaddingChipFg = Color(0xFFC2410C)
private val BackgroundChipBg = Color(0xFFCCFBF1)
private val BackgroundChipFg = Color(0xFF0F766E)
private val WidthChipBg = Color(0xFFEDE9FE)
private val WidthChipFg = Color(0xFF6D28D9)

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
    val isContainer = node is PlaygroundNode.Column || node is PlaygroundNode.Row
    val children: List<PlaygroundNode> = when (node) {
        is PlaygroundNode.Column -> node.children
        is PlaygroundNode.Row -> node.children
        is PlaygroundNode.Text -> emptyList()
    }
    val selected = node.id == selectedId

    val accentColor = when (node) {
        is PlaygroundNode.Column -> ColumnAccent
        is PlaygroundNode.Row -> RowAccent
        is PlaygroundNode.Text -> TextAccent
    }
    val badgeBg = when (node) {
        is PlaygroundNode.Column -> ColumnBadgeBg
        is PlaygroundNode.Row -> RowBadgeBg
        is PlaygroundNode.Text -> TextBadgeBg
    }
    val badgeFg = when (node) {
        is PlaygroundNode.Column -> ColumnBadgeFg
        is PlaygroundNode.Row -> RowBadgeFg
        is PlaygroundNode.Text -> TextBadgeFg
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
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selected)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                else
                    MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isDragging) 8.dp else 1.dp
            )
        ) {
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                                        onDrag = { _, delta ->
                                            dragOffsetY += delta.y
                                        },
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
                        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
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
                        }
                        if (isContainer) {
                            SmallIconButton(onClick = { expanded = !expanded }, size = 20) {
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowDown
                                    else Icons.Default.KeyboardArrowRight,
                                    contentDescription = if (expanded) "Collapse" else "Expand",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else {
                            Spacer(Modifier.width(20.dp))
                        }
                        Surface(
                            color = badgeBg,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = nodeLabel(node),
                                color = badgeFg,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = nodePreview(node),
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = if (node is PlaygroundNode.Text) FontStyle.Italic
                            else FontStyle.Normal,
                            color = if (node is PlaygroundNode.Text)
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                        if (isContainer && children.isNotEmpty()) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            ) {
                                Text(
                                    text = "${children.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(Modifier.width(2.dp))
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 10.dp),
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

                    if (isContainer) {
                        ComponentAddMenu(
                            buttonLabel = "Add child",
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onAdd = { kind ->
                                onAddChild(node.id, kind)
                            }
                        )
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        }

        if (isContainer && expanded) {
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
private fun ModifierChip(
    modifier: PlaygroundModifier,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    val (bg, fg) = when (modifier) {
        is PlaygroundModifier.Padding -> PaddingChipBg to PaddingChipFg
        is PlaygroundModifier.Background -> BackgroundChipBg to BackgroundChipFg
        is PlaygroundModifier.Width -> WidthChipBg to WidthChipFg
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        color = bg,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, fg.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 3.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = modifier.chipLabel(),
                style = MaterialTheme.typography.labelSmall,
                color = fg,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Surface(
                onClick = onRemove,
                shape = CircleShape,
                color = fg.copy(alpha = 0.12f),
                modifier = Modifier.size(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "×",
                        color = fg,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 10.sp
                    )
                }
            }
        }
    }
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
            CompositionLocalProvider(
                LocalContentColor provides resolvedTint
            ) { content() }
        } else {
            content()
        }
    }
}

private fun PlaygroundModifier.chipLabel(): String = when (this) {
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

private fun nodePreview(node: PlaygroundNode): String = when (node) {
    is PlaygroundNode.Column -> ""
    is PlaygroundNode.Row -> ""
    is PlaygroundNode.Text -> {
        val t = node.text.take(40)
        "\"$t${if (node.text.length > 40) "..." else ""}\""
    }
}

private fun PlaygroundNode.addModifier(mod: PlaygroundModifier): PlaygroundNode =
    updateModifiers(modifiers + mod)

private fun PlaygroundNode.removeModifier(index: Int): PlaygroundNode =
    updateModifiers(modifiers.toMutableList().also { it.removeAt(index) })

private fun PlaygroundNode.replaceModifier(index: Int, mod: PlaygroundModifier): PlaygroundNode =
    updateModifiers(modifiers.toMutableList().also { it[index] = mod })

private fun PlaygroundNode.updateModifiers(new: List<PlaygroundModifier>): PlaygroundNode =
    when (this) {
        is PlaygroundNode.Column -> copy(modifiers = new)
        is PlaygroundNode.Row -> copy(modifiers = new)
        is PlaygroundNode.Text -> copy(modifiers = new)
    }
