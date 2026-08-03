package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.layout
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcVisibilityModifierOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

object VisibilityApplier : RcModifierApplier<RcVisibilityModifierOperation> {
    private const val GONE = 0
    private const val VISIBLE = 1
    private const val INVISIBLE = 2
    private const val OVERRIDE_GONE = 16
    private const val OVERRIDE_VISIBLE = 32
    private const val OVERRIDE_INVISIBLE = 64

    @Composable
    override fun apply(
        operation: RcVisibilityModifierOperation,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        val visibility = context.resolveInt(operation.visibilityId)

        return when {
            isGone(visibility) -> currentModifier.layout { _, _ -> layout(0, 0) {} }
            isInvisible(visibility) -> currentModifier.alpha(0f)
            else -> currentModifier
        }
    }

    private fun isGone(value: Int): Boolean {
        if ((value shr 4) > 0) {
            return (value and OVERRIDE_GONE) == OVERRIDE_GONE
        }
        return value == GONE
    }

    private fun isInvisible(value: Int): Boolean {
        if ((value shr 4) > 0) {
            return (value and OVERRIDE_INVISIBLE) == OVERRIDE_INVISIBLE
        }
        return value == INVISIBLE
    }
}