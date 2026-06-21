package io.github.deanalvero.remotecomposeplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeEngine
import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcContainerEndOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawCircleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawLineOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcHeaderOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcHeightModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextDataOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.UnknownOperation

@Composable
fun RemoteComposeVisualizer(
    rcBytes: ByteArray,
    modifier: Modifier = Modifier
) {
    val discoveredOperations = remember(rcBytes) {
        RemoteComposeEngine.parseStream(rcBytes)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Total Operations Tracked: ${discoveredOperations.size}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        items(discoveredOperations) { op ->
            OperationRowItem(operation = op)
        }
    }
}

@Composable
private fun OperationRowItem(operation: RcOperation) {
    val badgeColor = when (operation) {
        is RcHeaderOperation -> Color(0xFF2196F3)
        is RcTextDataOperation -> Color(0xFFFF9800)
        is RcRootLayoutOperation,
        is RcLayoutContentOperation,
        is RcCanvasLayoutOperation,
        is RcCanvasContentOperation -> Color(0xFF00BCD4)
        is RcContainerEndOperation -> Color(0xFF607D8B)
        is RcRowLayoutOperation,
        is RcColumnLayoutOperation,
        is RcBoxLayoutOperation -> Color(0xFF4CAF50)
        is RcTextLayoutOperation -> Color(0xFFFFC107)
        is RcWidthModifierOperation,
        is RcHeightModifierOperation,
        is RcBackgroundModifierOperation,
        is RcPaddingModifierOperation -> Color(0xFF9C27B0)
        is RcDrawCircleOperation,
        is RcDrawLineOperation -> Color(0xFF7C3AED)
        is UnknownOperation -> Color(0xFFE91E63)
        else -> Color(0xFFE91E63)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(badgeColor, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (operation.opCode >= 0) operation.opCode.toString() else "!!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = operation.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = operation.toString(),
                    fontSize = 13.sp,
                    color = Color(0xFF555555),
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}
