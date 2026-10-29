package com.jonwal.dancetempo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TapArea(
    isBeat1Active: Boolean,
    onTap: (isBeat1: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val currentOnTap by rememberUpdatedState(onTap)

    // Vibrant solid colors for idle, noticeably deeper colors for pressed
    val beat1Idle = Color(0xFFFF6D00)     // Bright amber orange
    val beat1Pressed = Color(0xFFC43E00)  // Dark amber orange

    val mainIdle = Color(0xFF2563EB)      // Vibrant royal blue
    val mainPressed = Color(0xFF1D4ED8)   // Solid darker blue

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- 1. DEDICATED BEAT 1 AREA (when active) ---
        AnimatedVisibility(
            visible = isBeat1Active,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            var isBeat1Pressed by remember { mutableStateOf(false) }

            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(115.dp)
                    .shadow(
                        elevation = if (isBeat1Pressed) 1.dp else 6.dp,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .scale(if (isBeat1Pressed) 0.95f else 1.0f)
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            down.consume()
                            isBeat1Pressed = true
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentOnTap(true)
                            try {
                                waitForUpOrCancellation()
                            } finally {
                                isBeat1Pressed = false
                            }
                        }
                    },
                shape = RoundedCornerShape(16.dp),
                color = if (isBeat1Pressed) beat1Pressed else beat1Idle,
                border = BorderStroke(
                    width = 1.5.dp,
                    color = if (isBeat1Pressed) Color.White.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.3f)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "1",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "1. Schlag\n(Downbeat)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            lineHeight = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // --- 2. MAIN TAP AREA ---
        var isMainPressed by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .shadow(
                    elevation = if (isMainPressed) 1.dp else 6.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .scale(if (isMainPressed) 0.96f else 1.0f)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        down.consume()
                        isMainPressed = true
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        currentOnTap(false)
                        try {
                            waitForUpOrCancellation()
                        } finally {
                            isMainPressed = false
                        }
                    }
                },
            shape = RoundedCornerShape(16.dp),
            color = if (isMainPressed) mainPressed else mainIdle,
            border = BorderStroke(
                width = 1.5.dp,
                color = if (isMainPressed) Color.White.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.3f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isBeat1Active) "2, 3, 4..." else "TAP",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isBeat1Active) "Folgeschläge im Takt" else "Hier im Takt tippen",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.95f)
                    )
                }
            }
        }
    }
}
