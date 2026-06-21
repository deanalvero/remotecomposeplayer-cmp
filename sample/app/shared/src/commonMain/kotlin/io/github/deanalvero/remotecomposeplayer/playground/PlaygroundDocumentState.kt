package io.github.deanalvero.remotecomposeplayer.playground

data class PlaygroundDocumentState(
    val nodes: List<PlaygroundNode> = emptyList(),
    val selectedId: String? = null,
    val nextComponentId: Int = 1
) {
    fun select(id: String?): PlaygroundDocumentState = copy(selectedId = id)

    fun addRoot(kind: PlaygroundComponentKind): PlaygroundDocumentState {
        val node = createNode(kind, nextComponentId)
        return copy(
            nodes = nodes + node,
            selectedId = node.id,
            nextComponentId = nextComponentId + 1,
        )
    }

    fun addChild(parentId: String, kind: PlaygroundComponentKind): PlaygroundDocumentState {
        val node = createNode(kind, nextComponentId)
        val updated = nodes.addChild(parentId, node)
        return copy(
            nodes = updated,
            selectedId = node.id,
            nextComponentId = nextComponentId + 1,
        )
    }

    fun updateNode(nodeId: String, transform: (PlaygroundNode) -> PlaygroundNode): PlaygroundDocumentState {
        return copy(nodes = nodes.updateNode(nodeId, transform))
    }

    fun deleteNode(nodeId: String): PlaygroundDocumentState {
        val updated = nodes.deleteNode(nodeId)
        val selected = if (selectedId == nodeId) null else selectedId
        return copy(nodes = updated, selectedId = selected)
    }

    fun findNode(nodeId: String?): PlaygroundNode? {
        if (nodeId == null) return null
        return nodes.findNode(nodeId)
    }

    fun moveNode(nodeId: String, direction: Int): PlaygroundDocumentState {
        return copy(nodes = nodes.moveNode(nodeId, direction))
    }

    companion object {
        fun empty(): PlaygroundDocumentState = PlaygroundDocumentState()
    }
}

private fun createNode(kind: PlaygroundComponentKind, componentId: Int): PlaygroundNode {
    val id = "node-$componentId"
    return when (kind) {
        PlaygroundComponentKind.Column -> PlaygroundNode.Column(id = id, componentId = componentId)
        PlaygroundComponentKind.Row -> PlaygroundNode.Row(id = id, componentId = componentId)
        PlaygroundComponentKind.Box -> PlaygroundNode.Box(id = id, componentId = componentId)
        PlaygroundComponentKind.Canvas -> PlaygroundNode.Canvas(id = id, componentId = componentId)
        PlaygroundComponentKind.Text -> PlaygroundNode.Text(id = id, componentId = componentId)
        PlaygroundComponentKind.Spacer -> PlaygroundNode.Spacer(id = id, componentId = componentId)
    }
}

private fun List<PlaygroundNode>.findNode(nodeId: String): PlaygroundNode? {
    for (node in this) {
        if (node.id == nodeId) return node
        when (node) {
            is PlaygroundNode.Column -> node.children.findNode(nodeId)?.let { return it }
            is PlaygroundNode.Row -> node.children.findNode(nodeId)?.let { return it }
            is PlaygroundNode.Box -> node.children.findNode(nodeId)?.let { return it }
            is PlaygroundNode.Canvas -> Unit
            is PlaygroundNode.Text -> Unit
            is PlaygroundNode.Spacer -> Unit
        }
    }
    return null
}

private fun List<PlaygroundNode>.addChild(parentId: String, child: PlaygroundNode): List<PlaygroundNode> {
    var changed = false
    val updated = map { node ->
        when (node) {
            is PlaygroundNode.Column -> {
                if (node.id == parentId) {
                    changed = true
                    node.copy(children = node.children + child)
                } else {
                    val children = node.children.addChild(parentId, child)
                    if (children !== node.children) changed = true
                    node.copy(children = children)
                }
            }
            is PlaygroundNode.Row -> {
                if (node.id == parentId) {
                    changed = true
                    node.copy(children = node.children + child)
                } else {
                    val children = node.children.addChild(parentId, child)
                    if (children !== node.children) changed = true
                    node.copy(children = children)
                }
            }
            is PlaygroundNode.Box -> {
                if (node.id == parentId) {
                    changed = true
                    node.copy(children = node.children + child)
                } else {
                    val children = node.children.addChild(parentId, child)
                    if (children !== node.children) changed = true
                    node.copy(children = children)
                }
            }
            is PlaygroundNode.Canvas -> {
                if (node.id == parentId) {
                    changed = true
                    node
                } else node
            }
            is PlaygroundNode.Text -> node
            is PlaygroundNode.Spacer -> node
        }
    }
    return if (changed) updated else this
}

private fun List<PlaygroundNode>.updateNode(
    nodeId: String,
    transform: (PlaygroundNode) -> PlaygroundNode
): List<PlaygroundNode> {
    var changed = false
    val updated = map { node ->
        when (node) {
            is PlaygroundNode.Column -> {
                if (node.id == nodeId) {
                    changed = true
                    transform(node)
                } else {
                    val children = node.children.updateNode(nodeId, transform)
                    if (children !== node.children) changed = true
                    node.copy(children = children)
                }
            }
            is PlaygroundNode.Row -> {
                if (node.id == nodeId) {
                    changed = true
                    transform(node)
                } else {
                    val children = node.children.updateNode(nodeId, transform)
                    if (children !== node.children) changed = true
                    node.copy(children = children)
                }
            }
            is PlaygroundNode.Box -> {
                if (node.id == nodeId) {
                    changed = true
                    transform(node)
                } else {
                    val children = node.children.updateNode(nodeId, transform)
                    if (children !== node.children) changed = true
                    node.copy(children = children)
                }
            }
            is PlaygroundNode.Canvas -> {
                if (node.id == nodeId) {
                    changed = true
                    transform(node)
                } else node
            }
            is PlaygroundNode.Text -> {
                if (node.id == nodeId) {
                    changed = true
                    transform(node)
                } else node
            }
            is PlaygroundNode.Spacer -> {
                if (node.id == nodeId) {
                    changed = true
                    transform(node)
                } else node
            }
        }
    }
    return if (changed) updated else this
}

private fun List<PlaygroundNode>.deleteNode(nodeId: String): List<PlaygroundNode> {
    var changed = false
    val updated = buildList {
        for (node in this@deleteNode) {
            when (node) {
                is PlaygroundNode.Column -> {
                    if (node.id == nodeId) {
                        changed = true
                        continue
                    }
                    val children = node.children.deleteNode(nodeId)
                    if (children !== node.children) changed = true
                    add(node.copy(children = children))
                }
                is PlaygroundNode.Row -> {
                    if (node.id == nodeId) {
                        changed = true
                        continue
                    }
                    val children = node.children.deleteNode(nodeId)
                    if (children !== node.children) changed = true
                    add(node.copy(children = children))
                }
                is PlaygroundNode.Box -> {
                    if (node.id == nodeId) {
                        changed = true
                        continue
                    }
                    val children = node.children.deleteNode(nodeId)
                    if (children !== node.children) changed = true
                    add(node.copy(children = children))
                }
                is PlaygroundNode.Canvas -> {
                    if (node.id == nodeId) {
                        changed = true
                        continue
                    }
                    add(node)
                }
                is PlaygroundNode.Text -> {
                    if (node.id == nodeId) {
                        changed = true
                        continue
                    }
                    add(node)
                }
                is PlaygroundNode.Spacer -> {
                    if (node.id == nodeId) {
                        changed = true
                        continue
                    }
                    add(node)
                }
            }
        }
    }
    return if (changed) updated else this
}

private fun List<PlaygroundNode>.moveNode(nodeId: String, direction: Int): List<PlaygroundNode> {
    val idx = indexOfFirst { it.id == nodeId }
    if (idx >= 0) {
        val targetIdx = (idx + direction).coerceIn(0, lastIndex)
        if (targetIdx == idx) return this
        val mutable = toMutableList()
        val item = mutable.removeAt(idx)
        mutable.add(targetIdx, item)
        return mutable
    }
    var changed = false
    val updated = map { node ->
        when (node) {
            is PlaygroundNode.Column -> {
                val children = node.children.moveNode(nodeId, direction)
                if (children !== node.children) {
                    changed = true
                    node.copy(children = children)
                } else node
            }
            is PlaygroundNode.Row -> {
                val children = node.children.moveNode(nodeId, direction)
                if (children !== node.children) {
                    changed = true
                    node.copy(children = children)
                } else node
            }
            is PlaygroundNode.Box -> {
                val children = node.children.moveNode(nodeId, direction)
                if (children !== node.children) {
                    changed = true
                    node.copy(children = children)
                } else node
            }
            is PlaygroundNode.Canvas -> node
            is PlaygroundNode.Text -> node
            is PlaygroundNode.Spacer -> node
        }
    }
    return if (changed) updated else this
}
