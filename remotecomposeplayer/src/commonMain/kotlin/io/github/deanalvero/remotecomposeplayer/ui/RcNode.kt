package io.github.deanalvero.remotecomposeplayer.ui

import io.github.deanalvero.remotecomposeplayer.core.RcOperation

sealed class RcNode {
    abstract val operation: RcOperation
    abstract val modifiers: MutableList<RcOperation>

    data class Layout(
        override val operation: RcOperation,
        override val modifiers: MutableList<RcOperation> = mutableListOf(),
        val children: MutableList<RcNode> = mutableListOf()
    ) : RcNode()

    data class Leaf(
        override val operation: RcOperation,
        override val modifiers: MutableList<RcOperation> = mutableListOf()
    ) : RcNode()
}
