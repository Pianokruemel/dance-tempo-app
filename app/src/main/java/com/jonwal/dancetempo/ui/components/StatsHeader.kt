package com.jonwal.dancetempo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonwal.dancetempo.ui.theme.TextSecondary

@Composable
fun StatsHeader(
    bpm: Int?,
    meter: Int?,
    tpm: Int?,
    isBeat1Active: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatBox(
            value = bpm?.toString() ?: "--",
            label = "BPM (Beats)",
            modifier = Modifier.weight(1f)
        )
        StatBox(
            value = when {
                meter != null -> "$meter/4"
                isBeat1Active -> "..."
                else -> "Alle"
            },
            label = "Taktart",
            modifier = Modifier.weight(1f)
        )
        StatBox(
            value = tpm?.toString() ?: "--",
            label = "TPM (Takte)",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatBox(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}
