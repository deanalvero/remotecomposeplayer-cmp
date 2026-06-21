package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader

@Composable
actual fun FileUploader(
    show: Boolean,
    onDismiss: () -> Unit,
    onFileSelected: (ByteArray) -> Unit
) {
    if (show) {
        LaunchedEffect(Unit) {
            val input = document.createElement("input") as HTMLInputElement
            input.type = "file"
            input.accept = ".rc"
            input.onchange = {
                val file = input.files?.item(0)
                if (file != null) {
                    val reader = FileReader()
                    reader.onload = {
                        val buffer = reader.result as ArrayBuffer
                        val uint8Array = Uint8Array(buffer)
                        val bytes = ByteArray(uint8Array.length)
                        for (i in 0 until uint8Array.length) {
                            bytes[i] = uint8Array[i]
                        }
                        onFileSelected(bytes)
                    }
                    reader.readAsArrayBuffer(file)
                }
                onDismiss()
            }
            input.click()
        }
    }
}