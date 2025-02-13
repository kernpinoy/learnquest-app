package org.cutie.learnquest.minigames

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cutie.learnquest.minigames.Feedback

@Composable
fun FeedbackScreen(feedback: Feedback) {
    val backgroundColor = when (feedback) {
        Feedback.Correct -> Color(0xFF4CAF50) // Green
        Feedback.Incorrect -> Color(0xFFF44336) // Red
    }
    val message = when (feedback) {
        Feedback.Correct -> "Correct"
        Feedback.Incorrect -> "Incorrect"
    }
    val icon = when (feedback) {
        Feedback.Correct -> Icons.Default.Check
        Feedback.Incorrect -> Icons.Default.Close
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = message,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}