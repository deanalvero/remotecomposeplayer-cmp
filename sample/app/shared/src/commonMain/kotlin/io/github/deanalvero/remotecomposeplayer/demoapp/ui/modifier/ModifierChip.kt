package io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.chipLabel
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundModifier

private val PaddingChipBg = Color(0xFFFFF7ED)
private val PaddingChipFg = Color(0xFFC2410C)
private val BackgroundChipBg = Color(0xFFCCFBF1)
private val BackgroundChipFg = Color(0xFF0F766E)
private val WidthChipBg = Color(0xFFEDE9FE)
private val WidthChipFg = Color(0xFF6D28D9)
private val HeightChipBg = Color(0xFFFCE7F3)
private val HeightChipFg = Color(0xFFBE185D)

@Composable
fun ModifierChip(
    modifier: PlaygroundModifier,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    val (bg, fg) = when (modifier) {
        is PlaygroundModifier.Padding -> PaddingChipBg to PaddingChipFg
        is PlaygroundModifier.Background -> BackgroundChipBg to BackgroundChipFg
        is PlaygroundModifier.Width -> WidthChipBg to WidthChipFg
        is PlaygroundModifier.Height -> HeightChipBg to HeightChipFg
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        color = bg,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, fg.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 3.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = modifier.chipLabel(),
                style = MaterialTheme.typography.labelSmall,
                color = fg,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Surface(
                onClick = onRemove,
                shape = CircleShape,
                color = fg.copy(alpha = 0.12f),
                modifier = Modifier.size(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "×",
                        color = fg,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 10.sp
                    )
                }
            }
        }
    }
}