package org.cutie.learnquest.minigames.numbers.matchit

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
import org.cutie.learnquest.minigames.numbers.Number

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        startGame()
    }

    private fun startGame() {
        val numberList = listOf(
            Number("Nine", R.drawable.mit9),
            Number("Four", R.drawable.mit4),
            Number("Five", R.drawable.mit5),
            Number("Eight", R.drawable.mit8),
            Number("Six", R.drawable.mit6),
        )

        _gameState.value = GameState(
            numbers = numberList.shuffled(),
            currentNumberIndex = 0,
            score = 0,
            isGameOver = false
        )
    }

    fun submitAnswer(answer: String, playSound: (Int) -> Unit) {
        viewModelScope.launch {
            val currentNumber = _gameState.value.numbers[gameState.value.currentNumberIndex]

            val isCorrect = answer == currentNumber.name
            val feedback = if (isCorrect) Feedback.Correct else Feedback.Incorrect

            playSound(if (isCorrect) R.raw.tama else R.raw.mali)

            _gameState.update { state ->
                state.copy(feedback = feedback)
            }

            viewModelScope.launch {
                delay(1000L)

                _gameState.update { state ->
                    val nextIndex = state.currentNumberIndex + 1
                    val isGameOver = nextIndex >= state.numbers.size

                    state.copy(
                        feedback = null,
                        score = if (isCorrect) state.score + 1 else state.score,
                        currentNumberIndex = nextIndex,
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
                currentNumberIndex = 0,
                isGameOver = false,
                feedback = null
            )
        }
    }
}