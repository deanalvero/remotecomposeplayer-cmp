package io.github.deanalvero.remotecomposeplayer.ui

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcContainerEndOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation

fun buildRcTree(operations: List<RcOperation>): RcNode.Layout {
    val root = RcNode.Layout(RcRootLayoutOperation(0, 0))
    val stack = mutableListOf<RcNode.Layout>().apply { add(root) }

    for (op in operations) {
        when (op) {
            is RcContainerEndOperation -> {
                if (stack.size > 1) stack.removeAt(stack.lastIndex)
            }
            is RcLayoutContentOperation, is RcRowLayoutOperation, is RcColumnLayoutOperation -> {
                val newNode = RcNode.Layout(op)
                stack.last().children.add(newNode)
                stack.add(newNode)
            }
            else -> {
                stack.last().children.add(RcNode.Leaf(op))
            }
        }
    }
    return root
}