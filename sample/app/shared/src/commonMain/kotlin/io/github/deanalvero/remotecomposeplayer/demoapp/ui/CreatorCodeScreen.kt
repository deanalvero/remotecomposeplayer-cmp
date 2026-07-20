package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily

@Composable
fun CreatorCodeScreen(
    code: String,
    modifier: Modifier = Modifier
) {
    SelectionContainer(modifier = modifier) {
        Text(
            text = code,
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}