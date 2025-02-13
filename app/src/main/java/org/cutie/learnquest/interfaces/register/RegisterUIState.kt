package org.cutie.learnquest.interfaces.register

sealed class RegisterUIState {
    // Initial state when screen loads and between actions
    object Idle : RegisterUIState()

    // Shows when registration request is in progress
    object Loading : RegisterUIState()

    // Success state with optional success message
    data class Success(val message: String) : RegisterUIState()

    // Error state with error message
    data class Error(val message: String) : RegisterUIState()
}