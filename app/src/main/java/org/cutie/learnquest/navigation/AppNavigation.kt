package org.cutie.learnquest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.cutie.learnquest.data.CookieStorage
import org.cutie.learnquest.minigames.colors.matchit.ColorGameScreen
import org.cutie.learnquest.minigames.letters.matchit.LetterGameScreen
import org.cutie.learnquest.minigames.numbers.matchit.NumberGameScreen
import org.cutie.learnquest.minigames.shapes.matchit.ShapesGameScreen
import org.cutie.learnquest.navigation.screens.Activities
import org.cutie.learnquest.navigation.screens.Login
import org.cutie.learnquest.navigation.screens.MainMenu
import org.cutie.learnquest.navigation.screens.Materials
import org.cutie.learnquest.navigation.screens.Register

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Track if session has been checked
    val hasCheckedSession = remember { mutableStateOf(false) }

    // Loading state to delay navigation until session check is done
    val loading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val cookieStorage = CookieStorage(context)
        val sessionCookie = cookieStorage.getCookie("auth_session")

        // Set session check state and update loading state
        hasCheckedSession.value = sessionCookie != null
        loading.value = false // Once session is checked, stop loading
    }

    // Only show NavHost after session check is completed
    if (!loading.value) {
        NavHost(navController = navController, startDestination = if (hasCheckedSession.value) Screen.MainMenu.route else Screen.Login.route) {
            composable(Screen.Login.route) {
                Login(navController)
            }
            composable(Screen.MainMenu.route) {
                MainMenu(navController)
            }
            composable(Screen.Register.route) {
                Register(navController)
            }
            composable(Screen.Materials.route) {
                Materials(navController)
            }
            composable(Screen.Activities.route) {
                Activities(navController)
            }
            composable(Screen.ShapesGame.route) {
                ShapesGameScreen()
            }
            composable(Screen.LetterGame.route) {
                LetterGameScreen()
            }
            composable(Screen.NumbersGame.route) {
                NumberGameScreen()
            }
            composable(Screen.ColorGame.route) {
                ColorGameScreen()
            }
        }
    }
}
