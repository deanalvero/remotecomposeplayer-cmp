package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation

object BackgroundApplier : RcModifierApplier<RcBackgroundModifierOperation> {
    override fun apply(
        operation: RcBackgroundModifierOperation,
        currentModifier: Modifier,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        val color = if (operation.usesColorId) {
            Color(context.getColor(operation.colorId))
        } else {
            Color(operation.r, operation.g, operation.b, operation.a)
        }

        val shape = if (operation.shapeType == RcBackgroundModifierOperation.SHAPE_CIRCLE) {
            CircleShape
        } else {
            RectangleShape
        }

        return currentModifier.background(color, shape)
    }
}