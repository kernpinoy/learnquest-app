package org.cutie.learnquest.interfaces.materials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.cutie.learnquest.data.repository.ChartRepository

class MaterialViewModelFactory(private val chartRepository: ChartRepository) :
    ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MaterialViewModel::class.java)) {
                return MaterialViewModel(chartRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class");
        }
}