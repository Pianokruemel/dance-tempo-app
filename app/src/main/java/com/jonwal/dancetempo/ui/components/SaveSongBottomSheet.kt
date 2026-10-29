package com.jonwal.dancetempo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonwal.dancetempo.data.service.MediaPlaybackTracker
import com.jonwal.dancetempo.data.service.SessionScanResult
import com.jonwal.dancetempo.model.Dance
import com.jonwal.dancetempo.ui.theme.AccentOrange
import com.jonwal.dancetempo.ui.theme.PrimaryBlue
import com.jonwal.dancetempo.ui.theme.StatusIdealGreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SaveSongBottomSheet(
    bpm: Int,
    meter: Int,
    primaryDance: Dance,
    matchingDances: List<Dance>,
    onDismiss: () -> Unit,
    onSaveSong: (
        title: String,
        artist: String,
        bpm: Int,
        meter: Int,
        notes: String?,
        streamingUrl: String?,
        danceIds: List<String>
    ) -> Unit
) {
    val context = LocalContext.current
    val detectedTrack by MediaPlaybackTracker.currentTrack.collectAsState()

    var title by remember { mutableStateOf(detectedTrack?.title ?: "") }
    var artist by remember { mutableStateOf(detectedTrack?.artist ?: "") }
    var notes by remember { mutableStateOf("") }
    var streamingUrl by remember { mutableStateOf("") }

    var scanStatus by remember { mutableStateOf<SessionScanResult>(SessionScanResult.Idle) }

    // Multi-select dances: initially preselect the primary dance clicked
    val selectedDanceIds = remember { mutableStateListOf(primaryDance.id) }

    // Auto-scan on sheet launch
    LaunchedEffect(Unit) {
        scanStatus = SessionScanResult.Scanning
        delay(300) // Brief moment for visual clarity
        val result = MediaPlaybackTracker.scanForActiveSession(context)
        scanStatus = result
        if (result is SessionScanResult.Found) {
            if (title.isBlank()) title = result.track.title
            if (artist.isBlank()) artist = result.track.artist
            if (streamingUrl.isBlank() && !result.track.streamingUrl.isNullOrBlank()) {
                streamingUrl = result.track.streamingUrl
            }
        }
    }

    // Auto-hide success or not-found status banner after 5 seconds
    LaunchedEffect(scanStatus) {
        if (scanStatus is SessionScanResult.Found || scanStatus is SessionScanResult.NotFound) {
            delay(5000)
            scanStatus = SessionScanResult.Idle
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha = 0.3f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Header: Title & Tempo Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Song speichern",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryBlue.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "$bpm BPM · $meter/4",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- NOTIFICATION / SCAN STATUS BANNER ---
            AnimatedVisibility(
                visible = scanStatus !is SessionScanResult.Idle,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                when (val st = scanStatus) {
                    is SessionScanResult.Scanning -> {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrimaryBlue.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = PrimaryBlue
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Suche nach aktiver Musik-Session...",
                                    fontSize = 12.sp,
                                    color = PrimaryBlue
                                )
                            }
                        }
                    }

                    is SessionScanResult.Found -> {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StatusIdealGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, StatusIdealGreen.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = StatusIdealGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Session gefunden: ${st.track.title} (${st.track.artist})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = StatusIdealGreen
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        title = st.track.title
                                        artist = st.track.artist
                                        if (!st.track.streamingUrl.isNullOrBlank()) {
                                            streamingUrl = st.track.streamingUrl
                                        }
                                    },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Übernehmen", fontSize = 11.sp, color = StatusIdealGreen)
                                }
                            }
                        }
                    }

                    is SessionScanResult.NotFound -> {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Keine aktive Musik-Session gefunden. Bitte Titel manuell eingeben.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    is SessionScanResult.PermissionRequired -> {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentOrange.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AccentOrange.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = AccentOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Benachrichtigungszugriff erforderlich",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentOrange
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Um Titel aus Spotify/Player automatisch zu lesen, benötigt Android die Berechtigung für Benachrichtigungen.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Button(
                                    onClick = { MediaPlaybackTracker.openNotificationSettings(context) },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("In Android-Einstellungen aktivieren", fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }

            // Rescan Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        scanStatus = SessionScanResult.Scanning
                        val result = MediaPlaybackTracker.scanForActiveSession(context)
                        scanStatus = result
                        if (result is SessionScanResult.Found) {
                            title = result.track.title
                            artist = result.track.artist
                            if (!result.track.streamingUrl.isNullOrBlank()) {
                                streamingUrl = result.track.streamingUrl
                            }
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Nach Musik-Session suchen", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Songtitel *") },
                placeholder = { Text("z. B. Sway, Perfect, Despacito") },
                leadingIcon = { Icon(Icons.Default.MusicNote, contentDescription = null, tint = PrimaryBlue) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Artist Input
            OutlinedTextField(
                value = artist,
                onValueChange = { artist = it },
                label = { Text("Interpret / Band") },
                placeholder = { Text("z. B. Michael Bublé, Ed Sheeran") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Dance Selection Chips
            Text(
                text = "Tanzstile annotieren (Mehrfachauswahl möglich):",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Show matching dances + allow picking from all dances
            val displayedDances = (matchingDances + com.jonwal.dancetempo.model.DanceRepository.allDances).distinctBy { it.id }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                displayedDances.forEach { dance ->
                    val isSelected = selectedDanceIds.contains(dance.id)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) {
                                if (selectedDanceIds.size > 1) {
                                    selectedDanceIds.remove(dance.id)
                                }
                            } else {
                                selectedDanceIds.add(dance.id)
                            }
                        },
                        label = { Text(dance.name, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentOrange,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Spacer(modifier = Modifier.height(14.dp))

            // Notes Input
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notiz (optional)") },
                placeholder = { Text("z. B. Toller Akzent, gut für Einstufung") },
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Streaming Link Input
            OutlinedTextField(
                value = streamingUrl,
                onValueChange = { streamingUrl = it },
                label = { Text("Streaming-Link (Spotify / YouTube)") },
                placeholder = { Text("https://open.spotify.com/track/...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Save Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSaveSong(
                            title.trim(),
                            artist.trim(),
                            bpm,
                            meter,
                            notes.trim().ifBlank { null },
                            streamingUrl.trim().ifBlank { null },
                            selectedDanceIds.toList()
                        )
                        onDismiss()
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(
                    text = "In Bibliothek speichern",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
