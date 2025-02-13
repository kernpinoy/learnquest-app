package org.cutie.learnquest.navigation.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.cutie.learnquest.data.api.KtorApiClient
import org.cutie.learnquest.data.repository.ChartRepository
import org.cutie.learnquest.interfaces.materials.MaterialScreen
import org.cutie.learnquest.interfaces.materials.MaterialViewModel
import org.cutie.learnquest.interfaces.materials.MaterialViewModelFactory
import org.cutie.learnquest.interfaces.materials.PdfViewerScreen

@Composable
fun Materials(navController: NavController) {
    val context = LocalContext.current
    val materialViewModel: MaterialViewModel =
        viewModel(factory = MaterialViewModelFactory(ChartRepository(KtorApiClient(context))))
    val getChartsResult = materialViewModel.getChartsResult.value
    val selectedPdf = materialViewModel.selectedPdf.value
    val pdfResult = materialViewModel.getChartResult.value

    DisposableEffect(Unit) {
        materialViewModel.onCleanup = {
            try {
                val tempFiles = context.cacheDir.listFiles {
                    file -> file.name.startsWith("temp_")
                }

                tempFiles?.forEach { it.delete() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        onDispose {
            materialViewModel.onCleanup = null
        }
    }

    LaunchedEffect(Unit) {
        if (getChartsResult == null) {
            materialViewModel.fetchAllCharts()
        }
    }

    if (selectedPdf != null) {
        // Show PDF viewer
        PdfViewerScreen(
            pdfBytes = pdfResult?.getOrNull(),
            isLoading = pdfResult == null,
            error = pdfResult?.exceptionOrNull()?.message,
            fileName = selectedPdf.fileName,
            onBackPressed = { materialViewModel.clearSelectedPdf() }
        )
    } else {
        // Show material list
        MaterialScreen(
            charts = getChartsResult?.getOrNull() ?: emptyList(),
            isLoading = getChartsResult == null || materialViewModel.isRefreshing.value,
            onRefresh = { materialViewModel.refresh() },
            onPdfClick = { chart -> materialViewModel.fetchChart(chart) }
        )
    }
}