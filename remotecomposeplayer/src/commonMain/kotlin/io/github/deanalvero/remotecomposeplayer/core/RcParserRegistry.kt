package io.github.deanalvero.remotecomposeplayer.core

import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcContainerEndOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcHeaderOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextDataOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation

object RcParserRegistry {
    private val decoders = mutableMapOf<Int, RcOpDecoder>()

    init {
        register(RcHeaderOperation)
        register(RcRootLayoutOperation)
        register(RcRowLayoutOperation)
        register(RcColumnLayoutOperation)
        register(RcBackgroundModifierOperation)
        register(RcWidthModifierOperation)
        register(RcPaddingModifierOperation)
        register(RcLayoutContentOperation)
        register(RcTextDataOperation)
        register(RcTextLayoutOperation)
        register(RcContainerEndOperation)

    }

    fun register(decoder: RcOpDecoder) {
        decoders[decoder.opCode] = decoder
    }

    fun getDecoder(opCode: Int): RcOpDecoder? = decoders[opCode]
}
