package io.github.deanalvero.remotecomposeplayer.demoapp

import kotlinx.browser.document
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

actual fun downloadDocument(bytes: ByteArray, filename: String) {
    val uint8Array = Uint8Array(bytes.size)
    for (i in bytes.indices) {
        uint8Array[i] = bytes[i]
    }
    val blobParts = arrayOf<JsAny?>(uint8Array).toJsArray()

    val blob = Blob(
        blobParts = blobParts,
        options = BlobPropertyBag(type = "application/octet-stream")
    )

    val url = URL.createObjectURL(blob)

    val anchor = document.createElement("a") as HTMLAnchorElement
    anchor.href = url
    anchor.download = filename

    document.body?.appendChild(anchor)
    anchor.click()
    document.body?.removeChild(anchor)

    URL.revokeObjectURL(url)
}