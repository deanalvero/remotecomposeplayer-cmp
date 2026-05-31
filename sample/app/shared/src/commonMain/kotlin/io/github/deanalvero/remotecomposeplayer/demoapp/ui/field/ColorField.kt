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
    val hex = remember(value) { value.toUInt().toString(16).uppercase().padStart(8, '0') }
    var local by remember(value) { mutableStateOf(hex) }
    LaunchedEffect(value) {
        val normalized = value.toUInt().toString(16).uppercase().padStart(8, '0')
        if (local != normalized) local = normalized
    }
    OutlinedTextField(
        value = local,
        onValueChange = {
            local = it.uppercase()
            val parsed = it.trim().removePrefix("0x").removePrefix("#")
            parsed.toLongOrNull(16)?.let { raw ->
                onChange(raw.toInt())
            }
        },
        label = { Text("$label (AARRGGBB hex)") },
        modifier = Modifier.fillMaxWidth()
    )
}