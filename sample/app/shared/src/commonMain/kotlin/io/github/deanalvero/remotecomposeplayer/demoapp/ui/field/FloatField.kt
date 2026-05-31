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
fun FloatField(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    var local by remember(value) { mutableStateOf(value.toString()) }
    LaunchedEffect(value) {
        val normalized = value.toString()
        if (local != normalized) local = normalized
    }
    OutlinedTextField(
        value = local,
        onValueChange = {
            local = it
            it.trim().toFloatOrNull()?.let(onValueChange)
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}