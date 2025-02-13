package org.cutie.learnquest.interfaces.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.cutie.learnquest.data.repository.RegisterRepository


class RegisterScreenViewModelFactory(private val registerRepository: RegisterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterScreenViewModel::class.java)) {
            return RegisterScreenViewModel(registerRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}