package org.cutie.learnquest.navigation.screens

import MainMenuViewModelFactory
import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.cutie.learnquest.Augmented
import org.cutie.learnquest.data.api.KtorApiClient
import org.cutie.learnquest.data.repository.AuthRepository
import org.cutie.learnquest.interfaces.mainmenu.MainMenuScreen
import org.cutie.learnquest.interfaces.mainmenu.MainMenuViewModel
import org.cutie.learnquest.navigation.Screen

@Composable
fun MainMenu(navController: NavController) {
    val context = LocalContext.current.applicationContext // ✅ Safe Application Context
    val contextAgain = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val mainMenuViewModel: MainMenuViewModel = viewModel(
        factory = MainMenuViewModelFactory(
            context as Application, // ✅ Cast to Application
            AuthRepository(KtorApiClient(context))
        )
    )

    val logoutResult = mainMenuViewModel.logoutResult.value
    val isMusicPlaying = mainMenuViewModel.isMusicPlaying.value // ✅ UI observes ViewModel

    LaunchedEffect(logoutResult) {
        logoutResult?.let { result ->
            if (result.isSuccess) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.MainMenu.route) { inclusive = true }
                }
                mainMenuViewModel.resetData()
            } else {
                Toast.makeText(context, result.exceptionOrNull()?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    MainMenuScreen(
        isMusicPlaying = isMusicPlaying,
        onMusicToggle = { mainMenuViewModel.toggleMusic(contextAgain) },
        onArClicked = {
            mainMenuViewModel.stopMusic(contextAgain)
            coroutineScope.launch {
                val intent = Intent(contextAgain, Augmented::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        },
        onLearningMaterialsClicked = { navController.navigate(Screen.Materials.route) },
        onActivitiesClicked = { navController.navigate(Screen.Activities.route) },
        onLogoutClicked = { mainMenuViewModel.onLogOutClicked() }
    )
}