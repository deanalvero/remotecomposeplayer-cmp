package io.github.deanalvero.remotecomposeplayer.ui

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcContainerEndOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawCircleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawLineOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation

fun buildRcTree(operations: List<RcOperation>): RcNode.Layout {
    val root = RcNode.Layout(RcRootLayoutOperation(0, 0))
    val stack = mutableListOf(root)

    var lastAddedNode: RcNode = root

    for (op in operations) {
        when (op) {
            is RcContainerEndOperation -> {
                if (stack.size > 1) {
                    stack.removeAt(stack.lastIndex)
                }
                lastAddedNode = stack.last()
            }

            is RcPaddingModifierOperation,
            is RcBackgroundModifierOperation,
            is RcWidthModifierOperation -> {
                lastAddedNode.modifiers.add(op)
            }

            is RcRootLayoutOperation,
            is RcLayoutContentOperation,
            is RcCanvasLayoutOperation,
            is RcCanvasContentOperation,
            is RcRowLayoutOperation,
            is RcColumnLayoutOperation,
            is RcBoxLayoutOperation,
            is RcTextLayoutOperation -> {
                val newNode = RcNode.Layout(op)
                stack.last().children.add(newNode)
                stack.add(newNode)
                lastAddedNode = newNode
            }

            is RcDrawCircleOperation,
            is RcDrawLineOperation -> {
                if (stack.last().operation is RcLayoutContentOperation &&
                    stack.size >= 2 &&
                    stack[stack.lastIndex - 1].operation is RcCanvasLayoutOperation
                ) {
                    val leaf = RcNode.Leaf(op)
                    stack.last().children.add(leaf)
                    lastAddedNode = leaf
                }
            }

            else -> {
                val leaf = RcNode.Leaf(op)
                stack.last().children.add(leaf)
                lastAddedNode = leaf
            }
        }
    }
    return root
}
