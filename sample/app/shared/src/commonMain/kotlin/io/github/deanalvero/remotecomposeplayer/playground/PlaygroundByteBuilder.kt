package io.github.deanalvero.remotecomposeplayer.playground

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcContainerEndOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcHeaderOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextDataOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation
import io.github.deanalvero.remotecomposeplayer.writer.RcByteWriter

object PlaygroundByteBuilder {

    fun serialize(document: PlaygroundDocumentState): ByteArray {
        val writer = RcByteWriter()
        writeHeader(writer)
        writeOperation(writer, RcRootLayoutOperation(componentId = 0))
        document.nodes.forEach { node ->
            writeNode(writer, node)
        }
        return writer.toByteArray()
    }

    private fun writeHeader(writer: RcByteWriter) {
        writeOperation(
            writer,
            RcHeaderOperation(
                majorVersion = 1,
                minorVersion = 0,
                patchVersion = 0,
                legacyWidth = 1200,
                legacyHeight = 600,
                legacyCapabilities = 0L
            )
        )
    }

    private fun writeNode(writer: RcByteWriter, node: PlaygroundNode) {
        when (node) {
            is PlaygroundNode.Column -> {
                writeOperation(
                    writer,
                    RcColumnLayoutOperation(
                        componentId = node.componentId,
                        animationId = 0,
                        horizontalPositioning = node.horizontal,
                        verticalPositioning = node.vertical,
                        spacedBy = node.spacedBy
                    )
                )

                node.modifiers.forEach { modifier ->
                    writeOperation(writer, modifier.toOperation())
                }

                node.children.forEach { child -> writeNode(writer, child) }
                writeOperation(writer, RcContainerEndOperation())
            }

            is PlaygroundNode.Row -> {
                writeOperation(
                    writer,
                    RcRowLayoutOperation(
                        componentId = node.componentId,
                        animationId = 0,
                        horizontalPositioning = node.horizontal,
                        verticalPositioning = node.vertical,
                        spacedBy = node.spacedBy
                    )
                )

                node.modifiers.forEach { modifier ->
                    writeOperation(writer, modifier.toOperation())
                }

                node.children.forEach { child -> writeNode(writer, child) }
                writeOperation(writer, RcContainerEndOperation())
            }

            is PlaygroundNode.Text -> {
                writeOperation(
                    writer,
                    RcTextDataOperation(
                        textId = node.componentId,
                        text = node.text
                    )
                )
                writeOperation(
                    writer,
                    RcTextLayoutOperation(
                        componentId = node.componentId,
                        animationId = 0,
                        textId = node.componentId,
                        color = node.color,
                        fontSize = node.fontSize,
                        fontStyle = node.fontStyle,
                        fontWeight = node.fontWeight,
                        fontFamilyId = node.fontFamilyId,
                        textAlign = node.textAlign,
                        overflow = node.overflow,
                        maxLines = node.maxLines
                    )
                )

                node.modifiers.forEach { modifier ->
                    writeOperation(writer, modifier.toOperation())
                }
            }
        }
    }

    private fun writeOperation(writer: RcByteWriter, op: RcOperation) {
        when (op) {
            is RcHeaderOperation -> {
                writer.writeByte(op.opCode)
                if (op.properties.isEmpty()) {
                    writer.writeInt(op.majorVersion)
                    writer.writeInt(op.minorVersion)
                    writer.writeInt(op.patchVersion)
                    writer.writeInt(op.legacyWidth ?: 0)
                    writer.writeInt(op.legacyHeight ?: 0)
                    writer.writeLong(op.legacyCapabilities ?: 0L)
                } else {
                    error("Dynamic header serialization is not supported in the playground builder.")
                }
            }

            is RcRootLayoutOperation -> {
                writer.writeByte(op.opCode)
                writer.writeInt(op.componentId)
            }

            is RcColumnLayoutOperation -> {
                writer.writeByte(op.opCode)
                writer.writeInt(op.componentId)
                writer.writeInt(op.animationId)
                writer.writeInt(op.horizontalPositioning)
                writer.writeInt(op.verticalPositioning)
                writer.writeFloat(op.spacedBy)
            }

            is RcRowLayoutOperation -> {
                writer.writeByte(op.opCode)
                writer.writeInt(op.componentId)
                writer.writeInt(op.animationId)
                writer.writeInt(op.horizontalPositioning)
                writer.writeInt(op.verticalPositioning)
                writer.writeFloat(op.spacedBy)
            }

            is RcTextDataOperation -> {
                writer.writeByte(op.opCode)
                writer.writeInt(op.textId)
                writer.writeString(op.text)
            }

            is RcTextLayoutOperation -> {
                writer.writeByte(op.opCode)
                writer.writeInt(op.componentId)
                writer.writeInt(op.animationId)
                writer.writeInt(op.textId)
                writer.writeInt(op.color)
                writer.writeFloat(op.fontSize)
                writer.writeInt(op.fontStyle)
                writer.writeFloat(op.fontWeight)
                writer.writeInt(op.fontFamilyId)
                writer.writeInt(op.textAlign)
                writer.writeInt(op.overflow)
                writer.writeInt(op.maxLines)
            }

            is RcPaddingModifierOperation -> {
                writer.writeByte(op.opCode)
                writer.writeFloat(op.left)
                writer.writeFloat(op.top)
                writer.writeFloat(op.right)
                writer.writeFloat(op.bottom)
            }

            is RcBackgroundModifierOperation -> {
                writer.writeByte(op.opCode)
                writer.writeInt(op.flags)
                writer.writeInt(op.colorId)
                writer.writeInt(0)
                writer.writeInt(0)
                writer.writeFloat(op.r)
                writer.writeFloat(op.g)
                writer.writeFloat(op.b)
                writer.writeFloat(op.a)
                writer.writeInt(op.shapeType)
            }

            is RcWidthModifierOperation -> {
                writer.writeByte(op.opCode)
                writer.writeInt(op.typeId)
                writer.writeFloat(op.value)
            }

            is RcContainerEndOperation -> {
                writer.writeByte(op.opCode)
            }

            else -> error("Unsupported operation for serialization: " + op::class.simpleName)
        }
    }
}