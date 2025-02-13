package org.cutie.learnquest.minigames.letters.matchit

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
import org.cutie.learnquest.minigames.letters.Letter

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        startGame()
    }

    private fun startGame() {
        val letterList = listOf(
            Letter("E", R.drawable.mite),
            Letter("A", R.drawable.mita),
            Letter("G", R.drawable.mitg),
            Letter("M", R.drawable.mitm),
            Letter("Y", R.drawable.mity),
        )

        _gameState.value = GameState(
            letters = letterList.shuffled(),
            currentLetterIndex = 0,
            score = 0,
            isGameOver = false,
            feedback = null
        )
    }

    fun submitAnswer(answer: String, playSound: (Int) -> Unit) {
        viewModelScope.launch {
            val currentLetter = _gameState.value.letters[gameState.value.currentLetterIndex]

            val isCorrect = answer == currentLetter.name
            val feedback = if (isCorrect) Feedback.Correct else Feedback.Incorrect

            playSound(if (isCorrect) R.raw.tama else R.raw.mali)

            _gameState.update { state ->
                state.copy(feedback = feedback)
            }

            viewModelScope.launch {
                delay(1000L)

                _gameState.update { state ->
                    val nextIndex = state.currentLetterIndex + 1
                    val isGameOver = nextIndex >= state.letters.size

                    state.copy(
                        feedback = null,
                        score = if (isCorrect) state.score + 1 else state.score,
                        currentLetterIndex = nextIndex,
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
                currentLetterIndex = 0,
                isGameOver = false,
                feedback = null
            )
        }
    }
}