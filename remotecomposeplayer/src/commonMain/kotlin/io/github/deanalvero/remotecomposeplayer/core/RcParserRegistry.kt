package io.github.deanalvero.remotecomposeplayer.core

import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColorConstantOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColorExpressionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcComponentValueOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcContainerEndOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDataListIdsOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawCircleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawLineOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcFloatExpressionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcHeaderOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcHeightInModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcHeightModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLoopOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixRestoreOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixRotateOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixSaveOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixScaleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixSkewOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixTranslateOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcNamedVariableOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextDataOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextFromFloatOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLookupOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextMergeOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcThemeOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthInModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation

object RcParserRegistry {
    private val decoders = mutableMapOf<Int, RcOpDecoder>()

    init {
        listOf(
            RcHeaderOperation,
            RcRootLayoutOperation,
            RcRowLayoutOperation,
            RcColumnLayoutOperation,
            RcCanvasLayoutOperation,
            RcCanvasContentOperation,
            RcBackgroundModifierOperation,
            RcWidthModifierOperation,
            RcPaddingModifierOperation,
            RcLayoutContentOperation,
            RcTextDataOperation,
            RcTextLayoutOperation,
            RcDrawCircleOperation,
            RcDrawLineOperation,
            RcContainerEndOperation,
            RcTextFromFloatOperation,
            RcNamedVariableOperation,
            RcColorConstantOperation,
            RcThemeOperation,
            RcMatrixRestoreOperation,
            RcMatrixRotateOperation,
            RcMatrixSaveOperation,
            RcMatrixScaleOperation,
            RcMatrixSkewOperation,
            RcMatrixTranslateOperation,
            RcLoopOperation,
            RcColorExpressionOperation,
            RcDataListIdsOperation,
            RcFloatExpressionOperation,
            RcTextLookupOperation,
            RcTextMergeOperation,
            RcBoxLayoutOperation,
            RcHeightModifierOperation,
            RcComponentValueOperation,
            RcWidthInModifierOperation,
            RcHeightInModifierOperation
        ).forEach {
            register(it)
        }
    }

    fun register(decoder: RcOpDecoder) {
        decoders[decoder.opCode] = decoder
    }

    fun getDecoder(opCode: Int): RcOpDecoder? = decoders[opCode]
}
