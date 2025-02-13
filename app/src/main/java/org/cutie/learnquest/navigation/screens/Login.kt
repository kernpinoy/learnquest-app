package org.cutie.learnquest.navigation.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.cutie.learnquest.data.api.KtorApiClient
import org.cutie.learnquest.data.repository.AuthRepository
import org.cutie.learnquest.interfaces.login.LoginScreen
import org.cutie.learnquest.interfaces.login.LoginViewModel
import org.cutie.learnquest.interfaces.login.LoginViewModelFactory
import org.cutie.learnquest.navigation.Screen

@Composable
fun Login(navController: NavController) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(AuthRepository(KtorApiClient(context))))
    val loginForm = loginViewModel.loginForm.value
    val loginResult = loginViewModel.loginResult.value

    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            if (result.isSuccess) {
                navController.navigate(Screen.MainMenu.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }

                loginViewModel.resetData()
            } else {
                println("Login failed: ${result.exceptionOrNull()?.message}")
                Toast.makeText(context, result.exceptionOrNull()?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Pass the form and the function to update the form fields to the LoginScreen
    LoginScreen(
        onLoginClicked = {
            loginViewModel.onLoginClicked()
//            navController.navigate(Screen.MainMenu.route) {
//                popUpTo(Screen.Login.route) { inclusive = true }
//            }
        },
        onRegisterClicked = { navController.navigate(Screen.Register.route) },
        loginForm = loginForm,
        onFormFieldChange = { username, password ->
            loginViewModel.loginForm.value = loginForm.copy(username = username, password = password)
        }
    )
}