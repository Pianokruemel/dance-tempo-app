package com.jonwal.dancetempo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonwal.dancetempo.model.Dance
import com.jonwal.dancetempo.ui.theme.AccentOrange
import com.jonwal.dancetempo.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DanceFilterBottomSheet(
    allDancesAlphabetical: List<Dance>,
    selectedDanceIds: Set<String>,
    onToggleDance: (String) -> Unit,
    onSelectAll: () -> Unit,
    onDismiss: () -> Unit
) {
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
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💃 Tänze filtern",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextButton(
                    onClick = {
                        onSelectAll()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = if (selectedDanceIds.isEmpty()) "Alle aktiv" else "Alle anzeigen",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Alphabetical Dance List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                allDancesAlphabetical.forEach { dance ->
                    val isSelected = selectedDanceIds.contains(dance.id)

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) AccentOrange.copy(alpha = 0.15f) else Color.Transparent,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) AccentOrange else Color.White.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable { onToggleDance(dance.id) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = dance.name,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) AccentOrange else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = "${dance.meter}/4",
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { onToggleDance(dance.id) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AccentOrange,
                                    checkmarkColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
