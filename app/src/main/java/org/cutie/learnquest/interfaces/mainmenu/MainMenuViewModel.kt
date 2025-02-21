package org.cutie.learnquest.interfaces.mainmenu

import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.cutie.learnquest.data.repository.AuthRepository

class MainMenuViewModel(
    application: Application,
    private val authRepository: AuthRepository
) : AndroidViewModel(application) {

    val logoutResult: MutableState<Result<String>?> = mutableStateOf(null)
    val isMusicPlaying: MutableState<Boolean> = mutableStateOf(AudioManager.isPlaying()) // ✅ Persistent UI state

    init {
        AudioManager.initialize(application.applicationContext) // ✅ Ensure media is initialized
    }

    fun toggleMusic(ctx: Context) {
        AudioManager.toggleMusic(ctx)
        isMusicPlaying.value = AudioManager.isPlaying() // ✅ Ensure UI updates
    }

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

    fun stopMusic(ctx: Context) {
        AudioManager.stopMusic(ctx)
    }
}
