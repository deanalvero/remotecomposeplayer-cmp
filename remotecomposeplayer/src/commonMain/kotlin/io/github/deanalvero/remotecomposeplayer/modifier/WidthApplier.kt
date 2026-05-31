package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.common.RcDimensionType

object WidthApplier : RcModifierApplier<RcWidthModifierOperation> {
    override fun apply(
        operation: RcWidthModifierOperation,
        currentModifier: Modifier,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        return when (operation.dimensionType) {
            RcDimensionType.EXACT -> currentModifier.width(operation.value.dp)
            RcDimensionType.EXACT_DP -> currentModifier.width(operation.value.dp)
            RcDimensionType.FILL,
            RcDimensionType.FILL_PARENT_MAX_WIDTH -> currentModifier.fillMaxWidth()
            RcDimensionType.WRAP -> currentModifier.wrapContentWidth()
            RcDimensionType.WEIGHT -> {
                when (scope) {
                    is ColumnScope -> with(scope) { currentModifier.weight(operation.value) }
                    is RowScope -> with(scope) { currentModifier.weight(operation.value) }
                    else -> currentModifier
                }
            }
            else -> currentModifier
        }
    }
}