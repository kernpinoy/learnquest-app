package org.cutie.learnquest.minigames.shapes.matchit

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
import org.cutie.learnquest.minigames.shapes.Shape

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        startGame()
    }

    private fun startGame() {
        val shapeList = listOf(
            Shape("Circle", R.drawable.mitcircle),
            Shape("Diamond", R.drawable.mitdiamond),
            Shape("Square", R.drawable.mitsquare),
            Shape("Star", R.drawable.mitstar),
            Shape("Triangle", R.drawable.mittriangle)
        )

        _gameState.value = GameState(
            shapes = shapeList.shuffled(),
            currentShapeIndex = 0,
            score = 0,
            isGameOver = false,
            feedback = null
        )
    }

    fun submitAnswer(answer: String, playSound: (Int) -> Unit) {
        viewModelScope.launch {
            val currentShape = _gameState.value.shapes[gameState.value.currentShapeIndex]

            val isCorrect = answer == currentShape.name
            val feedback = if (isCorrect) Feedback.Correct else Feedback.Incorrect
            playSound(if (isCorrect) R.raw.tama else R.raw.mali)

            _gameState.update { state ->
                state.copy(feedback = feedback)
            }

            viewModelScope.launch {
                delay(1000L)

                _gameState.update { state ->
                    val nextIndex = state.currentShapeIndex + 1
                    val isGameOver = nextIndex >= state.shapes.size

                    state.copy(
                        feedback = null,
                        score = if (isCorrect) state.score + 1 else state.score,
                        currentShapeIndex = nextIndex,
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
                currentShapeIndex = 0,
                isGameOver = false,
                feedback = null
            )
        }
    }
}