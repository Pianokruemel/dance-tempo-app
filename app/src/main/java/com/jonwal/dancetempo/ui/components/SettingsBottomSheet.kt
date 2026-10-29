package com.jonwal.dancetempo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonwal.dancetempo.data.local.PreferencesManager
import com.jonwal.dancetempo.data.local.PreferredMusicApp
import com.jonwal.dancetempo.ui.theme.AccentOrange
import com.jonwal.dancetempo.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    onDismiss: () -> Unit,
    onExportJsonClicked: () -> Unit,
    onImportJsonClicked: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { PreferencesManager(context) }
    var preferredApp by remember { mutableStateOf(prefs.getPreferredMusicApp()) }

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
        ) {
            // Header
            Text(
                text = "⚙️ Einstellungen",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 1. BEVORZUGTE MUSIK-APP ---
            Text(
                text = "Standard Musik-App zum Abspielen",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Text(
                text = "Wird geöffnet, wenn du bei einem Song auf 'Abspielen' tippst.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            PreferredMusicApp.entries.forEach { app ->
                val isSelected = preferredApp == app
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) PrimaryBlue.copy(alpha = 0.15f) else Color.Transparent,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) PrimaryBlue else Color.White.copy(alpha = 0.08f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable {
                            preferredApp = app
                            prefs.setPreferredMusicApp(app)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                preferredApp = app
                                prefs.setPreferredMusicApp(app)
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = app.displayName,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- 2. IMPORT / EXPORT DER BIBLIOTHEK ---
            Text(
                text = "Bibliothek sichern & wiederherstellen",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentOrange
            )
            Text(
                text = "Exportiere deine Songs als JSON-Backup oder übertrage sie auf ein anderes Smartphone.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onExportJsonClicked()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export (JSON)", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = {
                        onImportJsonClicked()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, AccentOrange.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Import (JSON)", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. ÜBER & LIZENZ ---
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "DanceTempo v1.0.0",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Freie Open-Source-Software unter der GNU General Public License v3 (GPLv3). Vollständig mit KI-Pair-Programming entwickelt.",
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
