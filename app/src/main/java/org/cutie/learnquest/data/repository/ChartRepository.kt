package org.cutie.learnquest.data.repository

import org.cutie.learnquest.data.api.KtorApiClient

class ChartRepository(private val apiClient: KtorApiClient) {
    suspend fun getAllCharts(): Result<List<KtorApiClient.ChartMetadata>> {
        return try {
            apiClient.getCharts()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChart(filename: String): Result<ByteArray> {
        return try {
            apiClient.getChart(filename)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}