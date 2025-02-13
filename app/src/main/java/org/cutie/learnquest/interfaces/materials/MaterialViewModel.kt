package org.cutie.learnquest.interfaces.materials

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.cutie.learnquest.data.repository.ChartRepository
import org.cutie.learnquest.data.api.KtorApiClient

class MaterialViewModel(private val chartRepository: ChartRepository) : ViewModel() {
    val getChartsResult = mutableStateOf<Result<List<KtorApiClient.ChartMetadata>>?>(null)
    val getChartResult = mutableStateOf<Result<ByteArray>?>(null)
    val isRefreshing = mutableStateOf(false)
    val selectedPdf = mutableStateOf<KtorApiClient.ChartMetadata?>(null)
    var onCleanup: (() -> Unit)? = null // Callback to delete files

    fun fetchAllCharts() {
        viewModelScope.launch {
            val result = chartRepository.getAllCharts()
            getChartsResult.value = result
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            val result = chartRepository.getAllCharts()
            getChartsResult.value = result
            isRefreshing.value = false
        }
    }

    fun fetchChart(chart: KtorApiClient.ChartMetadata) {
        selectedPdf.value = chart
        viewModelScope.launch {
            getChartResult.value = null // Reset previous result
            val result = chartRepository.getChart(chart.fileName)
            getChartResult.value = result
        }
    }

    fun clearSelectedPdf() {
        selectedPdf.value = null
        getChartResult.value = null
    }

    fun resetData() {
        getChartResult.value = null
        getChartsResult.value = null
        isRefreshing.value = false
        selectedPdf.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            onCleanup?.invoke() // Trigger file deletion from UI layer
            resetData()
        }
    }

}