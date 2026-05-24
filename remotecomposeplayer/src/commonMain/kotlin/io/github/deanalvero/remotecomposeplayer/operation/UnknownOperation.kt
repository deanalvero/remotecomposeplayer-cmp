package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class UnknownOperation(
    override val opCode: Int,
    override val name: String = "UNKNOWN_OPCODE",
    val length: Int,
    val partialData: ByteArray
) : RcOperation {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UnknownOperation

        if (opCode != other.opCode) return false
        if (length != other.length) return false
        if (name != other.name) return false
        if (!partialData.contentEquals(other.partialData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opCode
        result = 31 * result + length
        result = 31 * result + name.hashCode()
        result = 31 * result + partialData.contentHashCode()
        return result
    }
}
