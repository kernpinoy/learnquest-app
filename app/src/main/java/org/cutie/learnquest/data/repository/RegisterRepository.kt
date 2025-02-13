package org.cutie.learnquest.data.repository

import org.cutie.learnquest.data.api.KtorApiClient

class RegisterRepository(private val apiClient: KtorApiClient) {
    suspend fun register(
        lrn: String,
        firstName: String,
        middleName: String,
        lastName: String,
        sex: String,
        password: String,
        classCode: String
    ): Result<String> {
        return try {
            apiClient.register(lrn, firstName, middleName, lastName, sex, password, classCode)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}