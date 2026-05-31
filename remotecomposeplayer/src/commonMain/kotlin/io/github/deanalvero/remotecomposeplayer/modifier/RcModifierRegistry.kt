package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation
import kotlin.reflect.KClass

object RcModifierRegistry {
    private val appliers = mutableMapOf<KClass<out RcOperation>, RcModifierApplier<*>>()

    init {
        register(RcPaddingModifierOperation::class, PaddingApplier)
        register(RcBackgroundModifierOperation::class, BackgroundApplier)
        register(RcWidthModifierOperation::class, WidthApplier)
    }

    private fun <T : RcOperation> register(type: KClass<T>, applier: RcModifierApplier<T>) {
        appliers[type] = applier
    }

    @Suppress("UNCHECKED_CAST")
    fun applyAll(
        operations: List<RcOperation>,
        initial: Modifier = Modifier,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        return operations.fold(initial) { mod, op ->
            val applier = appliers[op::class] as? RcModifierApplier<RcOperation>
            applier?.apply(op, mod, scope, context) ?: mod
        }
    }
}