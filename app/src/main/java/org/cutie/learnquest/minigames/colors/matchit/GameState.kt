package org.cutie.learnquest.minigames.colors.matchit

import org.cutie.learnquest.minigames.colors.Color
import org.cutie.learnquest.minigames.Feedback

data class GameState(
    val colors: List<Color> = emptyList(),
    val currentColorIndex: Int = 0,
    val score: Int = 0,
    val feedback: Feedback? = null,
    val isGameOver: Boolean = false,
)
