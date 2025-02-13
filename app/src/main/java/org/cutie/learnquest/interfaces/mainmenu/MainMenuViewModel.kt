package org.cutie.learnquest.interfaces.mainmenu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.cutie.learnquest.data.repository.AuthRepository

class MainMenuViewModel(private val authRepository: AuthRepository): ViewModel() {
    val logoutResult = mutableStateOf<Result<String>?>(null)

    fun onLogOutClicked() {
        logoutUser()
    }

    private fun logoutUser() {
        viewModelScope.launch {
            val result = authRepository.logout()
            logoutResult.value = result
        }
    }

    fun resetData() {
        logoutResult.value = null
    }
}