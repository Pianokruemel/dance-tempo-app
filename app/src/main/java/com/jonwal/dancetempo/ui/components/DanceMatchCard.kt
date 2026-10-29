package com.jonwal.dancetempo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonwal.dancetempo.model.DanceLevelMode
import com.jonwal.dancetempo.model.DanceMatch
import com.jonwal.dancetempo.model.TempoStatus
import com.jonwal.dancetempo.ui.theme.*

@Composable
fun DanceMatchCard(
    match: DanceMatch,
    onSaveClicked: ((DanceMatch) -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Row 1: Dance Name & Meter (never compressed by badges)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.dance.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "${match.dance.meter}/4",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Row 2: Status Badge, Half-speed notice, and Save Bookmark Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    val badgeColor = when (match.status) {
                        TempoStatus.IDEAL -> StatusIdealGreen
                        TempoStatus.SLOW -> StatusSlowBlue
                        TempoStatus.FAST -> StatusFastYellow
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = badgeColor.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, badgeColor.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = match.status.label,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = badgeColor
                        )
                    }

                    if (match.isHalfSpeed) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Halbtempo (${match.effectiveBpm / 2} → ${match.effectiveBpm})",
                            fontSize = 11.sp,
                            color = AccentOrange
                        )
                    }
                }

                if (onSaveClicked != null) {
                    IconButton(
                        onClick = { onSaveClicked(match) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "Song speichern",
                            tint = AccentOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Visual Scale Slider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // Background Track
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                )

                // Ideal Zone Highlight
                val idealStart = match.idealStartPercentage.coerceIn(0f, 1f)
                val idealWidth = (match.idealEndPercentage - match.idealStartPercentage).coerceAtLeast(0.05f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (idealStart + idealWidth).coerceIn(0f, 1f))
                        .padding(start = (idealStart * 280).dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(StatusIdealGreen.copy(alpha = 0.6f))
                )

                // Current BPM Marker
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = match.percentage.coerceIn(0f, 1f))
                        .wrapContentWidth(Alignment.End)
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color.White)
                    )
                }
            }

            // Scale Numbers Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Min: ${match.dance.bpmScaleMin}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "WDSF Ideal: ${match.dance.idealBpmMin}–${match.dance.idealBpmMax} BPM",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = StatusIdealGreen
                )
                Text(
                    text = "Max: ${match.dance.bpmScaleMax}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Rhythm hint (if the tempo overlaps with other dances)
            match.dance.rhythmHint?.let { hint ->
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AccentOrange.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentOrange.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "💡 Rhythmik: ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentOrange
                        )
                        Text(
                            text = hint,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
