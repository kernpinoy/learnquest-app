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
import org.cutie.learnquest.interfaces.mainmenu.AudioManager
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
    val hasCheckedSession = remember { mutableStateOf(false) }
    val loading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // ✅ Initialize AudioManager with context
        AudioManager.initialize(context)

        val cookieStorage = CookieStorage(context)
        val sessionCookie = cookieStorage.getCookie("auth_session")
        hasCheckedSession.value = sessionCookie != null
        loading.value = false
    }

    if (!loading.value) {
        NavHost(
            navController = navController,
            startDestination = if (hasCheckedSession.value) Screen.MainMenu.route else Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                AudioManager.stopMusic(context)
                Login(navController)
            }
            composable(Screen.MainMenu.route) {
                if (!AudioManager.isPlaying() && !AudioManager.wasPausedByUser()) {
                    AudioManager.startOrResumeMusic(context) // ✅ Pass context
                }
                MainMenu(navController)
            }
            composable(Screen.Register.route) {
                AudioManager.stopMusic(context)
                Register(navController)
            }
            composable(Screen.Materials.route) {
                if (!AudioManager.isPlaying() && !AudioManager.wasPausedByUser()) {
                    AudioManager.startOrResumeMusic(context)
                }
                Materials(navController)
            }
            composable(Screen.Activities.route) {
                if (!AudioManager.isPlaying() && !AudioManager.wasPausedByUser()) {
                    AudioManager.startOrResumeMusic(context)
                }
                Activities(navController)
            }
            composable(Screen.ShapesGame.route) {
                AudioManager.pauseMusic(context)
                ShapesGameScreen()
            }
            composable(Screen.LetterGame.route) {
                AudioManager.pauseMusic(context)
                LetterGameScreen()
            }
            composable(Screen.NumbersGame.route) {
                AudioManager.pauseMusic(context)
                NumberGameScreen()
            }
            composable(Screen.ColorGame.route) {
                AudioManager.pauseMusic(context)
                ColorGameScreen()
            }
        }
    }
}

