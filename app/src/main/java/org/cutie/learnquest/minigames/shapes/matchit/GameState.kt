package org.cutie.learnquest.minigames.shapes.matchit

import org.cutie.learnquest.minigames.Feedback
import org.cutie.learnquest.minigames.shapes.Shape

data class GameState(
    val shapes: List<Shape> = emptyList(),
    val currentShapeIndex: Int = 0,
    val score: Int = 0,
    val feedback: Feedback? = null,
    val isGameOver: Boolean = false
)
