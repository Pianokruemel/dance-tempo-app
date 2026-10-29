package com.jonwal.dancetempo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonwal.dancetempo.model.DanceMatch
import com.jonwal.dancetempo.model.DanceMismatch
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
    onSaveMismatchClicked: ((DanceMismatch) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showAiInfoDialog by remember { mutableStateOf(false) }

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showAiInfoDialog = true }
            ) {
                Text(
                    text = "💃 DanceTempo",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Über DanceTempo",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }

            FilterChip(
                selected = state.isBeat1ModeActive,
                onClick = { viewModel.toggleBeat1Mode() },
                label = {
                    Text(
                        text = if (state.isBeat1ModeActive) "1. Schlag aktiv" else "1. Schlag Feld",
                        fontSize = 12.sp,
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
                    // --- RHYTHM PATTERN BANNER (IF DETECTED) ---
                    state.detectedRhythm?.let { rhythm ->
                        item {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = com.jonwal.dancetempo.ui.theme.AccentOrange.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, com.jonwal.dancetempo.ui.theme.AccentOrange.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🥁",
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Rhythmus erkannt: ${rhythm.pattern.displayName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = com.jonwal.dancetempo.ui.theme.AccentOrange
                                        )
                                        Text(
                                            text = rhythm.pattern.description,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

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
                                    text = "Kein Tanz passt exakt auf ${state.currentBpm} BPM.",
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
                            DanceMismatchCard(
                                mismatch = mismatch,
                                onSaveClicked = onSaveMismatchClicked
                            )
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

    // AI & Open-Source Disclaimer Dialog
    if (showAiInfoDialog) {
        AlertDialog(
            onDismissRequest = { showAiInfoDialog = false },
            title = {
                Text(
                    text = "Über DanceTempo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "🤖 KI-Entwicklungs-Hinweis",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = com.jonwal.dancetempo.ui.theme.PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Diese App wurde vollständig unter Einsatz moderner KI-Coding-Tools (LLM Agentic Coding) entwickelt. Konzeption, Architektur, Kotlin/Compose-Code, Algorithmen und Tests entstanden im interaktiven Pair-Programming zwischen Entwickler und KI.",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "📄 Open Source & Lizenz",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = com.jonwal.dancetempo.ui.theme.AccentOrange
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "DanceTempo ist freie Open-Source-Software unter der GNU General Public License v3 (GPLv3). Ausgelegt auf Tanzschule, Welttanzprogramm (WTP) und Gesellschaftstanz.",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAiInfoDialog = false }) {
                    Text("Schließen")
                }
            }
        )
    }
}
