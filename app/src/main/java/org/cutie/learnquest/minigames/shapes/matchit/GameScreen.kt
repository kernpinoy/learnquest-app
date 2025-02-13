package org.cutie.learnquest.minigames.shapes.matchit

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import org.cutie.learnquest.minigames.shapes.Shape

@Composable
fun ShapesGameScreen() {
    val viewModel: GameViewModel = viewModel()
    val gameState by viewModel.gameState.collectAsState()
    val currentShape = gameState.shapes.getOrNull(gameState.currentShapeIndex)
    val choices = listOf(
        Shape("Circle", R.drawable.mit_ans_circle),
        Shape("Diamond", R.drawable.mit_ans_diamond),
        Shape("Square", R.drawable.mit_ans_square),
        Shape("Star", R.drawable.mit_ans_star),
        Shape("Triangle", R.drawable.mit_ans_triangle),
        Shape("Oblong", R.drawable.mit_ans_oblong),
    )
    val context = LocalContext.current

    fun playSound(soundRes: Int) {
        val mediaPlayer = MediaPlayer.create(context, soundRes)
        mediaPlayer.setOnCompletionListener { it.release() } // Free memory after playing
        mediaPlayer.start()
    }

    var timeLeft by remember { mutableIntStateOf(30) }

    val shuffledChoices = remember {
        choices.shuffled()
    }

    LaunchedEffect(Unit) {
        playSound(R.raw.hugis)
    }

    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver) {
            timeLeft = 30
        }
    }

    LaunchedEffect(gameState.currentShapeIndex) {
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

        currentShape != null -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
                    .background(color = Color(0xffffb344))
            ) {
                TimerDisplay(timeLeft = timeLeft)
                GameContent(
                    currentShape = currentShape,
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
    currentShape: Shape,
    options: List<Shape>,
    score: Int,
    onOptionSelected: (String, (Int) -> Unit) -> Unit,
    playSound: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = Color(0xffffb344))
    ) {
        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp)
        )
        Image(
            painter = painterResource(id = currentShape.imageRes),
            contentDescription = currentShape.name,
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
fun ShapesGameScreenPreview() {
    ShapesGameScreen()
}
