package org.cutie.learnquest.minigames.letters.matchit

import org.cutie.learnquest.minigames.letters.Letter
import org.cutie.learnquest.minigames.Feedback

data class GameState(
    val letters: List<Letter> = emptyList(),
    val currentLetterIndex: Int = 0,
    val score: Int = 0,
    val feedback: Feedback? = null,
    val isGameOver: Boolean = false
)