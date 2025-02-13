package org.cutie.learnquest.data.repository

import org.cutie.learnquest.data.api.KtorApiClient

class AuthRepository(private val apiClient: KtorApiClient) {
    suspend fun login(username: String, password: String): Result<String> {
        return try {
            apiClient.login(username, password)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<String> {
        return try {
            apiClient.logout()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}