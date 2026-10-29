package com.jonwal.dancetempo

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jonwal.dancetempo.data.local.DanceTempoDatabase
import com.jonwal.dancetempo.data.local.LibraryBackupHelper
import com.jonwal.dancetempo.model.DanceMatch
import com.jonwal.dancetempo.model.DanceMismatch
import com.jonwal.dancetempo.repository.SongRepository
import com.jonwal.dancetempo.ui.components.SaveSongBottomSheet
import com.jonwal.dancetempo.ui.components.SettingsBottomSheet
import com.jonwal.dancetempo.ui.screens.LibraryScreen
import com.jonwal.dancetempo.ui.screens.TapperScreen
import com.jonwal.dancetempo.ui.theme.AccentOrange
import com.jonwal.dancetempo.ui.theme.DanceTempoTheme
import com.jonwal.dancetempo.ui.theme.PrimaryBlue
import com.jonwal.dancetempo.viewmodel.DanceTempoViewModel
import com.jonwal.dancetempo.viewmodel.SongLibraryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
    TAPPER("Tapper"),
    LIBRARY("Bibliothek")
}

class MainActivity : ComponentActivity() {

    private val tempoViewModel: DanceTempoViewModel by viewModels()

    private val libraryViewModel: SongLibraryViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = DanceTempoDatabase.getInstance(applicationContext)
                val repo = SongRepository(db.songDao())
                @Suppress("UNCHECKED_CAST")
                return SongLibraryViewModel(repo) as T
            }
        }
    }

    private val exportJsonLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        if (uri != null) {
            lifecycleScope.launch {
                try {
                    val db = DanceTempoDatabase.getInstance(applicationContext)
                    val repo = SongRepository(db.songDao())
                    val songs = repo.songsStream.first()
                    val json = LibraryBackupHelper.exportToJson(songs)

                    contentResolver.openOutputStream(uri)?.use { out ->
                        out.write(json.toByteArray(Charsets.UTF_8))
                    }
                    Toast.makeText(this@MainActivity, "${songs.size} Songs exportiert!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Export fehlgeschlagen: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val importJsonLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            lifecycleScope.launch {
                try {
                    val json = contentResolver.openInputStream(uri)?.use { input ->
                        input.bufferedReader().readText()
                    } ?: ""

                    val parsed = LibraryBackupHelper.parseJson(json)
                    if (parsed.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Keine gültigen Songs in der Datei gefunden.", Toast.LENGTH_LONG).show()
                        return@launch
                    }

                    val db = DanceTempoDatabase.getInstance(applicationContext)
                    val repo = SongRepository(db.songDao())

                    var count = 0
                    parsed.forEach { (song, danceIds) ->
                        repo.saveSong(
                            title = song.title,
                            artist = song.artist,
                            bpm = song.bpm,
                            meter = song.meter,
                            notes = song.notes,
                            streamingUrl = song.streamingUrl,
                            danceIds = danceIds
                        )
                        count++
                    }

                    Toast.makeText(this@MainActivity, "$count Songs erfolgreich importiert!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Import fehlgeschlagen: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DanceTempoTheme {
                MainAppScaffold(
                    tempoViewModel = tempoViewModel,
                    libraryViewModel = libraryViewModel,
                    onSaveSongToRepo = { title, artist, bpm, meter, notes, url, dances ->
                        lifecycleScope.launch {
                            val db = DanceTempoDatabase.getInstance(applicationContext)
                            val repo = SongRepository(db.songDao())
                            repo.saveSong(title, artist, bpm, meter, notes, url, dances)
                            Toast.makeText(this@MainActivity, "Song '$title' gespeichert!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onExportJson = {
                        exportJsonLauncher.launch("dancetempo_backup.json")
                    },
                    onImportJson = {
                        importJsonLauncher.launch(arrayOf("application/json", "text/*"))
                    }
                )
            }
        }
    }
}

@Composable
fun MainAppScaffold(
    tempoViewModel: DanceTempoViewModel,
    libraryViewModel: SongLibraryViewModel,
    onSaveSongToRepo: (
        title: String,
        artist: String,
        bpm: Int,
        meter: Int,
        notes: String?,
        streamingUrl: String?,
        danceIds: List<String>
    ) -> Unit,
    onExportJson: () -> Unit,
    onImportJson: () -> Unit
) {
    var currentTab by remember { mutableStateOf(AppTab.TAPPER) }
    var matchToSave by remember { mutableStateOf<DanceMatch?>(null) }
    var mismatchToSave by remember { mutableStateOf<DanceMismatch?>(null) }
    var isSettingsOpen by remember { mutableStateOf(false) }
    val tempoState by tempoViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.TAPPER,
                    onClick = { currentTab = AppTab.TAPPER },
                    icon = { Icon(Icons.Default.Speed, contentDescription = "Tapper") },
                    label = { Text("Tapper") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = PrimaryBlue,
                        indicatorColor = PrimaryBlue
                    )
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.LIBRARY,
                    onClick = { currentTab = AppTab.LIBRARY },
                    icon = { Icon(Icons.Default.LibraryMusic, contentDescription = "Bibliothek") },
                    label = { Text("Bibliothek") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AccentOrange,
                        indicatorColor = AccentOrange
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentTab) {
                AppTab.TAPPER -> {
                    TapperScreen(
                        viewModel = tempoViewModel,
                        onSaveDanceClicked = { match -> matchToSave = match },
                        onSaveMismatchClicked = { mismatch -> mismatchToSave = mismatch }
                    )
                }
                AppTab.LIBRARY -> {
                    LibraryScreen(
                        viewModel = libraryViewModel,
                        onOpenSettings = { isSettingsOpen = true }
                    )
                }
            }
        }
    }

    // Settings BottomSheet
    if (isSettingsOpen) {
        SettingsBottomSheet(
            onDismiss = { isSettingsOpen = false },
            onExportJsonClicked = onExportJson,
            onImportJsonClicked = onImportJson
        )
    }

    // Save Song BottomSheet when user clicks Bookmark icon on a dance card
    val activeSaveDance = matchToSave?.dance ?: mismatchToSave?.dance
    if (activeSaveDance != null) {
        val currentBpm = tempoState.currentBpm ?: matchToSave?.effectiveBpm ?: activeSaveDance.idealBpmMin
        val currentMeter = tempoState.detectedMeter ?: activeSaveDance.meter
        val allMatchingDances = tempoState.matches.map { it.dance }.distinctBy { it.id }

        SaveSongBottomSheet(
            bpm = currentBpm,
            meter = currentMeter,
            primaryDance = activeSaveDance,
            matchingDances = if (allMatchingDances.isNotEmpty()) allMatchingDances else listOf(activeSaveDance),
            onDismiss = {
                matchToSave = null
                mismatchToSave = null
            },
            onSaveSong = { title, artist, bpm, meter, notes, url, dances ->
                onSaveSongToRepo(title, artist, bpm, meter, notes, url, dances)
                matchToSave = null
                mismatchToSave = null
            }
        )
    }
}
