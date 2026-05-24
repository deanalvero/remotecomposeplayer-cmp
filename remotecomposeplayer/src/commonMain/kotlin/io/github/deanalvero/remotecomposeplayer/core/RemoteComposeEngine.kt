package io.github.deanalvero.remotecomposeplayer.core

import io.github.deanalvero.remotecomposeplayer.operation.UnknownOperation

object RemoteComposeEngine {
    fun parseStream(rcData: ByteArray): List<RcOperation> {
        val reader = RcBufferReader(rcData)
        val operations = mutableListOf<RcOperation>()

        while (reader.hasRemaining) {
            val currentPos = reader.currentPosition
            try {
                val opCode = reader.readByte().toInt() and 0xFF
                val decoder = RcParserRegistry.getDecoder(opCode)

                if (decoder != null) {
                    operations.add(decoder.decode(reader))
                } else {
                    val rawData = reader.readBytes(8)
                    operations.add(UnknownOperation(opCode, "UNKNOWN_OPCODE", rawData.size, rawData))
                }
            } catch (e: Exception) {
                operations.add(UnknownOperation(-1, "PARSING_STREAM_TERMINATION_ERR: ${e.message}", 0, byteArrayOf()))
                break
            }
        }
        return operations
    }
}
