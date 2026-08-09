package io.github.deanalvero.remotecomposeplayer.demoapp.ui.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ColorField(
    label: String,
    value: Int,
    onChange: (Int) -> Unit
) {
    val upstreamHex = value.toUInt().toString(16).uppercase().padStart(8, '0')
    var local by remember { mutableStateOf(upstreamHex) }
    LaunchedEffect(value) {
        val localParsed = local.toLongOrNull(16)?.toInt()
        if (localParsed != value) {
            local = upstreamHex
        }
    }

    OutlinedTextField(
        value = local,
        onValueChange = { input ->
            val sanitized = input.trim().removePrefix("0x").removePrefix("#").uppercase()
                .filter { it in '0'..'9' || it in 'A'..'F' }
                .take(8)
            local = sanitized
            sanitized.toUIntOrNull(16)?.let { raw ->
                onChange(raw.toInt())
            }
        },
        label = { Text("$label (AARRGGBB hex)") },
        modifier = Modifier.fillMaxWidth()
    )
}