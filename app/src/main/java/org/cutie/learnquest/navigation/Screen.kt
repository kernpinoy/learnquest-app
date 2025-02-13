package org.cutie.learnquest.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object MainMenu : Screen("main_menu")
    object Register : Screen("register")
    object Materials : Screen("materials")
    object Activities : Screen("activities")
    object NumbersGame : Screen("numbers_game")
    object ShapesGame : Screen("shapes_game")
    object LetterGame : Screen("letter_game")
    object ColorGame : Screen("color_game")
}