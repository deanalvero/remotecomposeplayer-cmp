package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.runtime.Composable

@Composable
expect fun FileUploader(
    show: Boolean,
    onDismiss: () -> Unit,
    onFileSelected: (ByteArray) -> Unit
)