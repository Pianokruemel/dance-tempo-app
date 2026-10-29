package com.jonwal.dancetempo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonwal.dancetempo.model.Song
import com.jonwal.dancetempo.model.TempoStatus
import com.jonwal.dancetempo.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SongItemCard(
    song: Song,
    onClick: () -> Unit,
    activeDanceFilterId: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Column: Title, Artist, Dance Tags
            Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (song.artist.isNotBlank()) {
                    Text(
                        text = song.artist,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Dance tags
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    song.dances.forEach { dance ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = AccentOrange.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AccentOrange.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = dance.name,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = AccentOrange
                            )
                        }
                    }
                }
            }

            // Right Column: Color-Coded BPM Badge & Stars
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                val tempoStatus = song.getTempoStatus(activeDanceFilterId)
                val badgeColor = when (tempoStatus) {
                    TempoStatus.IDEAL -> StatusIdealGreen
                    TempoStatus.SLOW -> StatusSlowBlue
                    TempoStatus.FAST -> StatusFastYellow
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "${song.bpm} BPM",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                StarRatingBar(
                    rating = song.rating,
                    starSize = 14.dp
                )
            }
        }
    }
}
