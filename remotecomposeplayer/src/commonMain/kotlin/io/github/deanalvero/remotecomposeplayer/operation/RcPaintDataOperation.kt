package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcPaintDataOperation(
    override val opCode: Int = OP_CODE,
    val paintData: IntArray
) : RcOperation, CanvasScopedOperation {

    override val name: String = "PaintData"

    override fun toString(): String {
        return "$name -> [Bundle Size: ${paintData.size} ints]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 40
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val length = reader.readInt()
            val paintData = IntArray(length) { reader.readInt() }

            return RcPaintDataOperation(
                paintData = paintData
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as RcPaintDataOperation

        if (opCode != other.opCode) return false
        if (!paintData.contentEquals(other.paintData)) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opCode
        result = 31 * result + paintData.contentHashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
