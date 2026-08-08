package io.github.deanalvero.remotecomposeplayer.ui

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.CanvasScopedOperation
import io.github.deanalvero.remotecomposeplayer.operation.ModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBorderModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcClickModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcClipRectModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcContainerEndOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDataFloatOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDataIntOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcHeightModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcIntegerExpressionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcNamedVariableOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcScrollModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcStateLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextDataOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTouchExpressionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcValueFloatChangeActionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcValueIntegerChangeActionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcValueIntegerExpressionChangeActionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcValueStringChangeActionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcVisibilityModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation

fun buildRcTree(operations: List<RcOperation>): RcNode.Layout {
    val root = RcNode.Layout(RcRootLayoutOperation(0, 0))
    val stack = mutableListOf(root)

    var lastAddedNode: RcNode = root
    var activeClickModifier: RcClickModifierOperation? = null

    for (op in operations) {
        when (op) {
            is RcContainerEndOperation -> {
                if (activeClickModifier != null) {
                    activeClickModifier = null
                } else if (stack.size > 1) {
                    stack.removeAt(stack.lastIndex)
                    lastAddedNode = stack.last()
                }
            }
            is ModifierOperation -> {
                if (op is RcClickModifierOperation) {
                    activeClickModifier = op
                }
                lastAddedNode.modifiers.add(op)
            }

            is RcDataFloatOperation,
            is RcNamedVariableOperation,
            is RcDataIntOperation,
            is RcIntegerExpressionOperation,
            is RcTextDataOperation -> {}

            is RcValueIntegerChangeActionOperation,
            is RcValueIntegerExpressionChangeActionOperation,
            is RcValueStringChangeActionOperation,
            is RcValueFloatChangeActionOperation -> {
                val currentModifier: RcClickModifierOperation? = activeClickModifier
                if (currentModifier != null) {
                    val updatedModifier: RcClickModifierOperation = currentModifier.copy(
                        actions = currentModifier.actions + op
                    )
                    val modifierIndex = lastAddedNode.modifiers.indexOf(currentModifier)
                    if (modifierIndex != -1) {
                        lastAddedNode.modifiers[modifierIndex] = updatedModifier
                    }
                    activeClickModifier = updatedModifier
                }
            }

            is RcRootLayoutOperation,
            is RcLayoutContentOperation,
            is RcCanvasLayoutOperation,
            is RcCanvasContentOperation,
            is RcRowLayoutOperation,
            is RcColumnLayoutOperation,
            is RcBoxLayoutOperation,
            is RcStateLayoutOperation,
            is RcTextLayoutOperation,
            is RcTouchExpressionOperation -> {
                val newNode = RcNode.Layout(op)
                stack.last().children.add(newNode)
                stack.add(newNode)
                lastAddedNode = newNode
            }

            is CanvasScopedOperation -> {
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
