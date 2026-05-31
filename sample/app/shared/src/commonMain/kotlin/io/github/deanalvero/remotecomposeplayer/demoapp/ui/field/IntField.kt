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
fun IntField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    var local by remember(value) { mutableStateOf(value.toString()) }
    LaunchedEffect(value) {
        if (local != value.toString()) local = value.toString()
    }
    OutlinedTextField(
        value = local,
        onValueChange = {
            local = it
            it.trim().toIntOrNull()?.let(onValueChange)
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}