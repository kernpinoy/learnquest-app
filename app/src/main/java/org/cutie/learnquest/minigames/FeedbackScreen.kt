package org.cutie.learnquest.minigames

import androidx.compose.runtime.Composable

@Composable
fun FeedbackScreen(feedback: Feedback) {
    when (feedback) {
        Feedback.Correct -> CorrectAnswer()
        Feedback.Incorrect -> IncorrectAnswer()
    }
}