package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcLoopOperation(
    override val opCode: Int = OP_CODE,
    val indexVariableId: Int,
    val from: Float,
    val step: Float,
    val until: Float
) : RcOperation {

    override val name: String = "Loop"

    override fun toString(): String {
        return "Loop[indexId=$indexVariableId, from=$from, step=$step, until=$until)"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 215
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val indexVariableId = reader.readInt()
            val from = reader.readFloat()
            val step = reader.readFloat()
            val until = reader.readFloat()

            if (!from.isNaN() && !step.isNaN() && !until.isNaN()) {
                require(step != 0f) { "Loop step cannot be zero" }
                require(!(step < 0f && from < until)) {
                    "Loop step is negative but from < until"
                }
            }

            return RcLoopOperation(
                indexVariableId = indexVariableId,
                from = from,
                step = step,
                until = until
            )
        }
    }
}