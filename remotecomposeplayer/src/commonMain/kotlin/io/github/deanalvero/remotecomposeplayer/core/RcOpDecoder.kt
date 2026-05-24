package io.github.deanalvero.remotecomposeplayer.core

interface RcOpDecoder {
    val opCode: Int
    fun decode(reader: RcBufferReader): RcOperation
}