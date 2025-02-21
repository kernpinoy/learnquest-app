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
fun IncorrectAnswer() {
    val bgColor =
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFED5D47),  // Start color
                Color(0xFFC80871)   // End color
            ),
            start = Offset(0f, 0f),  // Top of the screen
            end = Offset(0f, Float.POSITIVE_INFINITY)  // Bottom of the screen
        )

    Box(
        modifier = Modifier.fillMaxSize().background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.sad),
                contentDescription = "Sad",
                modifier = Modifier.size(300.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.wrong_word),
                contentDescription = "Incorrect",
                modifier = Modifier.size(300.dp),
            )
        }
    }
}

@Preview
@Composable
fun IncorrectAnswerPreview() {
    IncorrectAnswer()
}
