package com.jonwal.dancetempo.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonwal.dancetempo.model.DanceRepository
import com.jonwal.dancetempo.model.SongSortOrder
import com.jonwal.dancetempo.ui.components.SongDetailBottomSheet
import com.jonwal.dancetempo.ui.components.SongItemCard
import com.jonwal.dancetempo.ui.theme.AccentOrange
import com.jonwal.dancetempo.ui.theme.PrimaryBlue
import com.jonwal.dancetempo.viewmodel.SongLibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: SongLibraryViewModel,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var isMixerOpen by remember { mutableStateOf(false) }
    var isDanceFilterOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // --- 1. HEADER ROW: Title, Search, Sort ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "📚 Tanz-Bibliothek",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "${state.songs.size} / ${state.totalCount}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Tanz-Mixer Button (Aktiv ab 2 Songs, sonst ausgegraut / disabled)
                IconButton(
                    onClick = { isMixerOpen = true },
                    enabled = state.totalCount > 1
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Tanz-Mixer",
                        tint = if (state.totalCount > 1) AccentOrange else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }

                // Search Toggle
                IconButton(onClick = { viewModel.toggleSearchActive() }) {
                    Icon(
                        imageVector = if (state.isSearchActive) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = "Suche",
                        tint = if (state.isSearchActive) AccentOrange else MaterialTheme.colorScheme.onSurface
                    )
                }

                // Settings Button
                IconButton(onClick = onOpenSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Einstellungen",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Search Input Field (when search is toggled on)
        if (state.isSearchActive) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder = { Text("Songtitel, Interpret oder Notiz suchen...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(10.dp)
            )
        }

        // --- 2. CONTROL PILLS: SORTIERUNG & TÄNZE AUSWÄHLEN ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Pille 1: Sortierung (BPM / Neueste)
            Box(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PrimaryBlue.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { sortMenuExpanded = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = state.sortOrder.displayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryBlue
                        )
                    }
                }

                DropdownMenu(
                    expanded = sortMenuExpanded,
                    onDismissRequest = { sortMenuExpanded = false }
                ) {
                    SongSortOrder.entries.forEach { order ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = order.displayName,
                                    fontWeight = if (state.sortOrder == order) FontWeight.Bold else FontWeight.Normal,
                                    color = if (state.sortOrder == order) PrimaryBlue else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                viewModel.setSortOrder(order)
                                sortMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // Pille 2: Tänze auswählen (Öffnet Sheet mit allen Tänzen alphabetisch)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AccentOrange.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, AccentOrange.copy(alpha = 0.5f)),
                modifier = Modifier
                    .weight(1f)
                    .clickable { isDanceFilterOpen = true }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "💃",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (state.selectedDanceIds.isEmpty()) "Alle Tänze" else "${state.selectedDanceIds.size} Tänze aktiv",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentOrange
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        // --- 3. SONG LIST ---
        if (state.songs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.totalCount == 0) {
                        "Noch keine Songs in der Bibliothek.\n\nTippe im 'Tapper' einen Rhythmus und speichere deine Lieblingssongs mit dem Lesezeichen-Icon ab!"
                    } else {
                        "Keine Songs für die aktuelle Filterauswahl gefunden."
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                val singleSelectedDanceId = if (state.selectedDanceIds.size == 1) state.selectedDanceIds.first() else null

                items(state.songs, key = { it.id }) { song ->
                    SongItemCard(
                        song = song,
                        activeDanceFilterId = singleSelectedDanceId,
                        onClick = { viewModel.selectSongForDetail(song) }
                    )
                }
            }
        }
    }

    // Dance Filter BottomSheet (Alphabetical Selection)
    if (isDanceFilterOpen) {
        val alphabeticalDances = remember { DanceRepository.allDances.sortedBy { it.name } }
        com.jonwal.dancetempo.ui.components.DanceFilterBottomSheet(
            allDancesAlphabetical = alphabeticalDances,
            selectedDanceIds = state.selectedDanceIds,
            onToggleDance = { viewModel.toggleDanceFilter(it) },
            onSelectAll = { viewModel.clearDanceFilter() },
            onDismiss = { isDanceFilterOpen = false }
        )
    }

    // Playlist Mixer BottomSheet
    if (isMixerOpen) {
        com.jonwal.dancetempo.ui.components.PlaylistMixerBottomSheet(
            songs = state.songs,
            onDismiss = { isMixerOpen = false }
        )
    }

    // Song Detail / Delete BottomSheet
    state.selectedSongForDetail?.let { song ->
        SongDetailBottomSheet(
            song = song,
            onDismiss = { viewModel.selectSongForDetail(null) },
            onDelete = { viewModel.deleteSong(it) }
        )
    }
}
