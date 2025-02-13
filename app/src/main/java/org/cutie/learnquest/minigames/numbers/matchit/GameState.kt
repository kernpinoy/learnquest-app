package org.cutie.learnquest.minigames.numbers.matchit

import org.cutie.learnquest.minigames.Feedback
import org.cutie.learnquest.minigames.numbers.Number

data class GameState (
    val numbers: List<Number> = emptyList(),
    val currentNumberIndex: Int = 0,
    val score: Int = 0,
    val feedback: Feedback? = null,
    val isGameOver: Boolean = false
)