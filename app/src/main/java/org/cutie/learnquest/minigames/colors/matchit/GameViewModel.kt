package org.cutie.learnquest.minigames.colors.matchit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.cutie.learnquest.R
import org.cutie.learnquest.minigames.Feedback
import org.cutie.learnquest.minigames.colors.Color

class GameViewModel: ViewModel()  {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        startGame()
    }

    private fun startGame() {
        var colorList = listOf(
            Color("Violet", R.drawable.mit_violet),
            Color("Red", R.drawable.mit_red),
            Color("Green", R.drawable.mit_green),
            Color("Yellow", R.drawable.mit_yellow),
            Color("Orange", R.drawable.mit_orange),
        )

        _gameState.value = GameState(
            colors = colorList.shuffled(),
            currentColorIndex = 0,
            score = 0,
            isGameOver = false,
            feedback = null
        )
    }

    fun submitAnswer(answer: String, playSound: (Int) -> Unit) {
        viewModelScope.launch {
            val currentLetter = _gameState.value.colors[gameState.value.currentColorIndex]

            val isCorrect = answer == currentLetter.name
            val feedback = if (isCorrect) Feedback.Correct else Feedback.Incorrect

            playSound(if (isCorrect) R.raw.tama else R.raw.mali)

            _gameState.update { state ->
                state.copy(feedback = feedback)
            }

            viewModelScope.launch {
                delay(1000L)

                _gameState.update { state ->
                    val nextIndex = state.currentColorIndex + 1
                    val isGameOver = nextIndex >= state.colors.size

                    state.copy(
                        feedback = null,
                        score = if (isCorrect) state.score + 1 else state.score,
                        currentColorIndex = nextIndex,
                        isGameOver = isGameOver
                    )
                }
            }
        }
    }

    fun restartGame() {
        startGame()
        _gameState.update {
            it.copy(
                score = 0,
                currentColorIndex = 0,
                isGameOver = false,
                feedback = null
            )
        }
    }
}