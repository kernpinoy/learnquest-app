package org.cutie.learnquest.interfaces.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.cutie.learnquest.models.LoginForm
import org.cutie.learnquest.data.repository.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val loginForm = mutableStateOf(LoginForm())
    val loginResult = mutableStateOf<Result<String>?>(null)

    fun onLoginClicked() {
        val form = loginForm.value

        if (form.isValid()) {
            // Trigger login
            loginUser(form.username, form.password)
        } else {
            // Handle invalid form case
            loginResult.value = Result.failure(Exception("Invalid credentials"))
        }
    }

    private fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            // Call the repository for login action
            val result = authRepository.login(username, password)
            loginResult.value = result
        }
    }

    fun resetData() {
        loginForm.value = LoginForm()
        loginResult.value = null
    }
}

