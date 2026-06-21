package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcHeightModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.common.RcDimensionType

object HeightApplier : RcModifierApplier<RcHeightModifierOperation> {
    override fun apply(
        operation: RcHeightModifierOperation,
        currentModifier: Modifier,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        return when (operation.dimensionType) {
            RcDimensionType.EXACT -> currentModifier.height(operation.value.dp)
            RcDimensionType.EXACT_DP -> currentModifier.height(operation.value.dp)
            RcDimensionType.FILL,
            RcDimensionType.FILL_PARENT_MAX_HEIGHT -> currentModifier.fillMaxHeight()
            RcDimensionType.WRAP -> currentModifier.wrapContentHeight()
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