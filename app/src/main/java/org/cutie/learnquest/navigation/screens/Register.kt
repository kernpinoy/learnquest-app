package org.cutie.learnquest.navigation.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.cutie.learnquest.data.api.KtorApiClient
import org.cutie.learnquest.data.repository.RegisterRepository
import org.cutie.learnquest.interfaces.register.RegisterScreen
import org.cutie.learnquest.interfaces.register.RegisterScreenViewModel
import org.cutie.learnquest.interfaces.register.RegisterScreenViewModelFactory
import org.cutie.learnquest.interfaces.register.RegisterUIState

@Composable
fun Register(navController: NavController) {
    val context = LocalContext.current
    val registerViewModel: RegisterScreenViewModel =
        viewModel(factory = RegisterScreenViewModelFactory(RegisterRepository(KtorApiClient(context))))

    val uiState = registerViewModel.uiState.collectAsState().value
    val registerForm = registerViewModel.registerForm.value
    val validationErrors = registerViewModel.validationErrors.value


    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUIState.Success -> {
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
            }
            is RegisterUIState.Error -> {
                Toast.makeText(
                    context,
                    uiState.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {} // Handle other states if needed
        }
    }

    RegisterScreen(
        registerForm = registerForm,
        validationErrors = validationErrors,
        isLoading = uiState is RegisterUIState.Loading,
        onGoBackClicked = { navController.popBackStack() },
        onFormFieldChange = { updatedForm ->
            registerViewModel.updateForm { updatedForm }
        },
        onRegisterClick = {
            registerViewModel.onRegisterClicked()
        }
    )
}