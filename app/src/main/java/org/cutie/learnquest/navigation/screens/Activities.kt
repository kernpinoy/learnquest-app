package org.cutie.learnquest.navigation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.cutie.learnquest.interfaces.activities.ActivitiesScreen
import org.cutie.learnquest.navigation.Screen

@Composable
fun Activities(navController: NavController) {
    ActivitiesScreen(
        onBackClicked = {
            navController.popBackStack()
        },
        onShapesClicked = {
           navController.navigate(Screen.ShapesGame.route)
        },
        onLettersClicked = {
            navController.navigate(Screen.LetterGame.route)
        },
        onNumberClicked = {
            navController.navigate(Screen.NumbersGame.route)
        },
        onColorClicked = {
            navController.navigate(Screen.ColorGame.route)
        }
    )
}