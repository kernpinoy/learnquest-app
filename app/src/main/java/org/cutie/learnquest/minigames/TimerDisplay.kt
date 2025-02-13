package org.cutie.learnquest.minigames

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerDisplay(timeLeft: Int) {
    Text(
        text = "Time Left: $timeLeft seconds",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Red,
        modifier = Modifier.padding(8.dp)
    )
}