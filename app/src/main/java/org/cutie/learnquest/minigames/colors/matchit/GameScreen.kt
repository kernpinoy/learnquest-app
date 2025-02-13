package org.cutie.learnquest.minigames.colors.matchit

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import org.cutie.learnquest.R
import org.cutie.learnquest.minigames.FeedbackScreen
import org.cutie.learnquest.minigames.GameOverScreen
import org.cutie.learnquest.minigames.TimerDisplay
import org.cutie.learnquest.minigames.colors.Color

@Composable
fun ColorGameScreen() {
    val viewModel: GameViewModel = viewModel()
    val gameState by viewModel.gameState.collectAsState()
    val currentColor = gameState.colors.getOrNull(gameState.currentColorIndex)
    val choices = listOf(
        Color("Violet", R.drawable.mit_ans_violet),
        Color("Red", R.drawable.mit_ans_red),
        Color("Green", R.drawable.mit_ans_green),
        Color("Yellow", R.drawable.mit_ans_yellow),
        Color("Orange", R.drawable.mit_ans_orange),
        Color("Blue", R.drawable.mit_ans_blue),
    )
    val context = LocalContext.current

    var timeLeft by remember { mutableIntStateOf(30) }

    val shuffledChoices = remember {
        choices.shuffled() // Shuffle the predefined choices
    }

    fun playSound(soundRes: Int) {
        val mediaPlayer = MediaPlayer.create(context, soundRes)
        mediaPlayer.setOnCompletionListener { it.release() } // Free memory after playing
        mediaPlayer.start()
    }

    LaunchedEffect(Unit) {
        playSound(R.raw.color)
    }

    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver) {
            timeLeft = 30
        }
    }

    LaunchedEffect(gameState.currentColorIndex) {
        if (!gameState.isGameOver) {
            timeLeft = 30
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
        }

        if (timeLeft == 0) {
            viewModel.submitAnswer("", ::playSound)
        }
    }

    when {
        gameState.isGameOver -> {
            GameOverScreen(score = gameState.score, onRestart = viewModel::restartGame)
        }

        gameState.feedback != null -> {
            FeedbackScreen(feedback = gameState.feedback!!)
        }

        currentColor != null -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize().background(color = ComposeColor(0xffffb344))
            ) {
                TimerDisplay(timeLeft = timeLeft)
                GameContent(
                    currentColor = currentColor,
                    options = shuffledChoices,
                    score = gameState.score,
                    onOptionSelected = viewModel::submitAnswer,
                    playSound = ::playSound
                )
            }
        }
    }
}

@Composable
fun GameContent(
    currentColor: Color,
    options: List<Color>,
    score: Int,
    onOptionSelected: (String, (Int) -> Unit) -> Unit,
    playSound: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = ComposeColor(0xffffb344))

    ) {
        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp)
        )
        Image(
            painter = painterResource(id = currentColor.imageRes),
            contentDescription = currentColor.name,
            modifier = Modifier
                .size(350.dp)
                .padding(bottom = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(count = options.size) { index ->
                val option = options[index]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = option.imageRes),
                        contentDescription = option.name,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable { onOptionSelected(option.name, playSound) }
                    )
                    Text(
                        text = option.name,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorGameScreenPreview() {
    ColorGameScreen()
}