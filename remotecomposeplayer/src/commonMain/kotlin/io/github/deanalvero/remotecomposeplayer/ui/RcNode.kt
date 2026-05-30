package io.github.deanalvero.remotecomposeplayer.ui

import io.github.deanalvero.remotecomposeplayer.core.RcOperation

sealed class RcNode {
    data class Layout(val operation: RcOperation, val children: MutableList<RcNode> = mutableListOf()) : RcNode()
    data class Leaf(val operation: RcOperation) : RcNode()
}