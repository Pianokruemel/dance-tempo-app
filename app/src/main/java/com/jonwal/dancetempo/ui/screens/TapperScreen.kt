package com.jonwal.dancetempo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonwal.dancetempo.model.DanceMatch
import com.jonwal.dancetempo.ui.components.DanceMatchCard
import com.jonwal.dancetempo.ui.components.DanceMismatchCard
import com.jonwal.dancetempo.ui.components.StatsHeader
import com.jonwal.dancetempo.ui.components.TapArea
import com.jonwal.dancetempo.ui.theme.AccentOrange
import com.jonwal.dancetempo.viewmodel.DanceTempoViewModel

@Composable
fun TapperScreen(
    viewModel: DanceTempoViewModel,
    onSaveDanceClicked: (DanceMatch) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // --- 1. TOP HEADER: App Title, Hobby/Turnier Switch & Beat-1 Mode Toggle ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💃 DanceTempo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Hobby / Turnier Toggle Chip
                val isHobby = state.danceLevelMode == com.jonwal.dancetempo.model.DanceLevelMode.HOBBY
                FilterChip(
                    selected = true,
                    onClick = {
                        val nextMode = if (isHobby) {
                            com.jonwal.dancetempo.model.DanceLevelMode.TOURNAMENT
                        } else {
                            com.jonwal.dancetempo.model.DanceLevelMode.HOBBY
                        }
                        viewModel.setDanceLevelMode(nextMode)
                    },
                    label = {
                        Text(
                            text = if (isHobby) "Hobby" else "Turnier (WDSF)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (isHobby) com.jonwal.dancetempo.ui.theme.PrimaryBlue else com.jonwal.dancetempo.ui.theme.StatusIdealGreen,
                        selectedLabelColor = androidx.compose.ui.graphics.Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                FilterChip(
                    selected = state.isBeat1ModeActive,
                    onClick = { viewModel.toggleBeat1Mode() },
                    label = {
                        Text(
                            text = if (state.isBeat1ModeActive) "1. Schlag an" else "1. Schlag",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentOrange,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        // --- 2. STATS HEADER: BPM, Taktart, TPM ---
        StatsHeader(
            bpm = state.currentBpm,
            meter = state.detectedMeter,
            tpm = state.currentTpm,
            isBeat1Active = state.isBeat1ModeActive
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        // Reset Button Row
        if (state.tapCount > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { viewModel.reset() },
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        modifier = Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Reset",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // --- 3. MIDDLE AREA: SCROLLABLE RESULTS (Matches + Non-Matches with Reasons) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (state.tapCount < 3) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tippe unten mindestens 3× gleichmäßig im Rhythmus...",
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    // --- SECTION A: PASSENDE TÄNZE ---
                    if (state.matches.isNotEmpty()) {
                        item {
                            Text(
                                text = "Passende Tänze:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                            )
                        }

                        items(items = state.matches, key = { "match_${it.dance.id}" }) { match ->
                            DanceMatchCard(
                                match = match,
                                danceLevelMode = state.danceLevelMode,
                                onSaveClicked = onSaveDanceClicked
                            )
                        }
                    } else {
                        item {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Kein Turniertanz passt exakt auf ${state.currentBpm} BPM.",
                                    modifier = Modifier.padding(10.dp),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // --- SECTION B: WEITERE TÄNZE (MISMATCHES MIT BEGRÜNDUNG) ---
                    if (state.mismatches.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Weitere Tänze (${state.mismatches.size} nicht passend):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        items(items = state.mismatches, key = { "mismatch_${it.dance.id}" }) { mismatch ->
                            DanceMismatchCard(mismatch = mismatch)
                        }
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        // --- 4. BOTTOM AREA: TAP PADS (Ergonomic thumb zone) ---
        TapArea(
            isBeat1Active = state.isBeat1ModeActive,
            onTap = { isBeat1 -> viewModel.onUserTap(isBeat1) }
        )
    }
}
