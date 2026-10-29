package com.jonwal.dancetempo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingBar(
    rating: Int,
    onRatingChanged: ((Int) -> Unit)? = null,
    maxStars: Int = 5,
    starSize: Dp = 26.dp,
    tint: Color = Color(0xFFFBBF24), // Vibrant gold/amber
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Default.Star else Icons.Outlined.Star
            val starTint = if (isSelected) tint else Color.White.copy(alpha = 0.25f)

            Icon(
                imageVector = icon,
                contentDescription = "$i Sterne",
                tint = starTint,
                modifier = Modifier
                    .size(starSize)
                    .padding(horizontal = 2.dp)
                    .then(
                        if (onRatingChanged != null) {
                            Modifier.clickable { onRatingChanged(i) }
                        } else Modifier
                    )
            )
        }
    }
}
