package org.cutie.learnquest.interfaces.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.cutie.learnquest.data.repository.RegisterRepository
import org.cutie.learnquest.models.RegisterForm
import androidx.compose.runtime.State

class RegisterScreenViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUIState>(RegisterUIState.Idle)
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    private val _registerForm = mutableStateOf(RegisterForm())
    val registerForm: State<RegisterForm> = _registerForm

    private val _validationErrors = mutableStateOf<List<String>>(emptyList())
    val validationErrors: State<List<String>> = _validationErrors

    fun updateForm(formUpdate: RegisterForm.() -> RegisterForm) {
        _registerForm.value = formUpdate(_registerForm.value)
        validateForm()
    }

    private fun validateForm() {
        when (val result = _registerForm.value.validate()) {
            is RegisterForm.ValidationResult.Valid -> {
                _validationErrors.value = emptyList()
            }
            is RegisterForm.ValidationResult.Invalid -> {
                _validationErrors.value = result.errors.map { it.getMessage() }
            }
        }
    }

    fun onRegisterClicked() {
        val form = _registerForm.value
        println("form: $form");
        when (val result = form.validate()) {
            is RegisterForm.ValidationResult.Valid -> {
                viewModelScope.launch {
                    try {
                        _uiState.value = RegisterUIState.Loading
                        val request = form.toRegistrationRequest()
                        val response = registerRepository.register(
                            request.lrn,
                            request.firstName,
                            request.middleName,
                            request.lastName,
                            request.sex.lowercase(),
                            request.password,
                            request.classCode
                        )
                        println("response: $response")
                        response.fold(
                            onSuccess = { message ->
                                _uiState.value = RegisterUIState.Success(message)
                            },
                            onFailure = { exception ->
                                _uiState.value = RegisterUIState.Error(exception.message ?: "Registration failed")
                            }
                        )
                    } catch (e: Exception) {
                        _uiState.value = RegisterUIState.Error(e.message ?: "An unexpected error occurred")
                    }
                }
            }
            is RegisterForm.ValidationResult.Invalid -> {
                _validationErrors.value = result.errors.map { it.getMessage() }
            }
        }
    }

    fun resetData() {
        _registerForm.value = RegisterForm()
        _validationErrors.value = emptyList()
        _uiState.value = RegisterUIState.Idle
    }
}