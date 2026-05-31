package io.github.deanalvero.remotecomposeplayer.demoapp.ui

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
fun TextFieldValueEditor(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    var local by remember(value) { mutableStateOf(value) }
    LaunchedEffect(value) {
        if (local != value) local = value
    }
    OutlinedTextField(
        value = local,
        onValueChange = {
            local = it
            onChange(it)
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}