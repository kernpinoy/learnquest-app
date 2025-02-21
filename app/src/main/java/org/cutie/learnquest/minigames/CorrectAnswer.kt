package org.cutie.learnquest.minigames

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cutie.learnquest.R

@Composable
fun CorrectAnswer() {
    val bgColor = Brush.linearGradient(
        colors = listOf(
            Color(0xFF92AD65),  // Start color
            Color(0xFF005733)   // End color
        ),
        start = Offset(0f, 0f),  // Top of the screen
        end = Offset(0f, Float.POSITIVE_INFINITY)  // Bottom of the screen
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.smile),
                contentDescription = "Smile",
                modifier = Modifier.size(300.dp),
            )
            Image(
                painter = painterResource(id = R.drawable.correct_word),
                contentDescription = "Correct",
                modifier = Modifier.size(300.dp),
            )
        }
    }
}

@Preview
@Composable
fun CorrectAnswerPreview() {
    CorrectAnswer()
}