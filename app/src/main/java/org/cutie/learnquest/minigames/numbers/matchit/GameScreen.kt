package org.cutie.learnquest.minigames.numbers.matchit

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import org.cutie.learnquest.minigames.numbers.Number
import kotlin.Int

@Composable
fun NumberGameScreen() {
    val viewModel: GameViewModel = viewModel()
    val gameState by viewModel.gameState.collectAsState()
    val currentNumber = gameState.numbers.getOrNull(gameState.currentNumberIndex)
    val choices = listOf(
        Number("Five", R.drawable.mit_ans_5),
        Number("Four", R.drawable.mit_ans_4),
        Number("Nine", R.drawable.mit_ans_9),
        Number("Eight", R.drawable.mit_ans_8),
        Number("Six", R.drawable.mit_ans_6),
        Number("Two", R.drawable.mit_ans_2),
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
        playSound(R.raw.numbers)
    }

    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver) {
            timeLeft = 30
        }
    }

    LaunchedEffect(gameState.currentNumberIndex) {
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

        currentNumber != null -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
                    .background(color = Color(0xffffb344))
            ) {
                TimerDisplay(timeLeft = timeLeft)
                GameContent(
                    currentNumber = currentNumber,
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
    currentNumber: Number,
    options: List<Number>,
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
            .background(color = Color(0xffffb344))
    ) {
        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp)
        )
        Image(
            painter = painterResource(id = currentNumber.imageRes),
            contentDescription = currentNumber.name,
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
fun NumberGameScreenPreview() {
    NumberGameScreen()
}
