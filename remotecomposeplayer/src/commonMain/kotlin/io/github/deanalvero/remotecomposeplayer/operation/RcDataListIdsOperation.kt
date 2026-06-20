package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDataListIdsOperation(
    override val opCode: Int = OP_CODE,
    val id: Int,
    val ids: IntArray
) : RcOperation {

    override val name: String = "IdListData"

    override fun toString(): String {
        return "map[$id]  \"${ids.contentToString()}\""
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 146
        override val opCode: Int = OP_CODE
        private const val MAX_LIST = 2000

        override fun decode(reader: RcBufferReader): RcOperation {
            val id = reader.readInt()
            val len = reader.readInt()
            require(len <= MAX_LIST) { "$len list entries more than max = $MAX_LIST" }

            val ids = IntArray(len) { reader.readInt() }
            return RcDataListIdsOperation(
                id = id,
                ids = ids
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as RcDataListIdsOperation

        if (opCode != other.opCode) return false
        if (id != other.id) return false
        if (!ids.contentEquals(other.ids)) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opCode
        result = 31 * result + id
        result = 31 * result + ids.contentHashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}