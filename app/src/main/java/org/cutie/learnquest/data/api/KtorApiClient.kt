package org.cutie.learnquest.data.api

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.cutie.learnquest.data.CookieStorage
import org.cutie.learnquest.data.remote.Login
import org.cutie.learnquest.data.remote.Register

class KtorApiClient(private val context: Context) {
    @Serializable
    data class LoginResponse(
        val message: String
    )

    @Serializable
    data class LogoutResponse(
        val message: String
    )

    @Serializable
    data class RegisterResponse(
        val message: String
    )

    @Serializable
    data class ChartMetadata(
        val fileName: String,
        val fileLink: String
    )

    @Serializable
    data class ErrorResponse(
        val message: String
    )

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.ANDROID
        }
    }

    suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response: HttpResponse = httpClient.post(Routes.Login.fullUrl()) {
                contentType(ContentType.Application.Json)
                setBody(Login(username, password))
            }

            println("login response: $response")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val authCookie = response.headers["Set-Cookie"]
                    if (authCookie != null) {
                        val cookieStorage = CookieStorage(context)
                        cookieStorage.saveCookie("auth_session", authCookie)
                    }

                    val loginResponse: LoginResponse = response.body()
                    Result.success(loginResponse.message)
                }

                HttpStatusCode.Unauthorized -> {
                    val errorResponse: LoginResponse = response.body()
                    Result.failure(Exception(errorResponse.message))
                }

                HttpStatusCode.BadRequest -> {
                    val errorResponse: LoginResponse = response.body()
                    Result.failure(Exception(errorResponse.message))
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<String> {
        return try {
            val cookieStorage = CookieStorage(context)
            val sessionCookie = cookieStorage.getCookie("auth_session")

            if (sessionCookie == null) {
                return Result.failure(Exception("No auth session cookie found."))
            }

            val response: HttpResponse = httpClient.post(Routes.Logout.fullUrl()) {
                contentType(ContentType.Application.Json)
                header("Cookie", sessionCookie)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val logoutResponse: LogoutResponse = response.body()

                    cookieStorage.deleteCookie("auth_session")

                    Result.success(logoutResponse.message)
                }

                HttpStatusCode.Unauthorized -> {
                    val logoutResponse: LogoutResponse = response.body()
                    Result.failure(Exception(logoutResponse.message))
                }

                HttpStatusCode.Forbidden -> {
                    val logoutResponse: LogoutResponse = response.body()
                    Result.failure(Exception(logoutResponse.message))
                }

                HttpStatusCode.InternalServerError -> {
                    val logoutResponse: LogoutResponse = response.body()
                    Result.failure(Exception(logoutResponse.message))
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
            val response: HttpResponse = httpClient.post(Routes.Register.fullUrl()) {
                contentType(ContentType.Application.Json)
                setBody(
                    Register(
                        lrn, firstName, middleName, lastName, sex, password, classCode
                    )
                )
            }

            println("register response: $response");

            when (response.status) {
                HttpStatusCode.BadRequest -> {
                    val registerResponse: RegisterResponse = response.body()
                    Result.failure(Exception(registerResponse.message))
                }

                HttpStatusCode.Conflict -> {
                    val registerResponse: RegisterResponse = response.body()
                    Result.failure(Exception(registerResponse.message))
                }

                HttpStatusCode.InternalServerError -> {
                    val logoutResponse: LogoutResponse = response.body()
                    Result.failure(Exception(logoutResponse.message))
                }

                HttpStatusCode.Created -> {
                    val logoutResponse: LogoutResponse = response.body()
                    Result.success(logoutResponse.message)
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCharts(): Result<List<ChartMetadata>> {
        return try {
            val cookieStorage = CookieStorage(context)
            val sessionCookie = cookieStorage.getCookie("auth_session")

            if (sessionCookie == null) {
                return Result.failure(Exception("No auth session cookie found."))
            }

            val response: HttpResponse = httpClient.get(Routes.Charts.fullUrl()) {
                header("Cookie", sessionCookie)
            }

            println("getCharts response: $response")

            if (response.status == HttpStatusCode.OK) {
                println("response is ok")
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val charts: List<ChartMetadata> = response.body()
                    Result.success(charts)
                }

                HttpStatusCode.Unauthorized -> {
                    val errorResponse: ErrorResponse = response.body()
                    Result.failure(Exception(errorResponse.message))
                }

                HttpStatusCode.Forbidden -> {
                    val errorResponse: ErrorResponse = response.body()
                    Result.failure(Exception(errorResponse.message))
                }

                HttpStatusCode.InternalServerError -> {
                    Result.failure(Exception("Internal Server Error occurred."))
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChart(filename: String): Result<ByteArray> {
        return try {
            val cookieStorage = CookieStorage(context)
            val sessionCookie = cookieStorage.getCookie("auth_session")

            if (sessionCookie == null) {
                return Result.failure(Exception("No auth session cookie found."))
            }

            val response: HttpResponse = httpClient.get("${Routes.Charts.fullUrl()}/$filename") {
                header("Cookie", sessionCookie)
            }

            println("getChart response: $response")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val pdfBytes: ByteArray = response.body() // Get the file as ByteArray
                    Result.success(pdfBytes)
                }

                HttpStatusCode.NotFound -> {
                    val errorResponse: ErrorResponse = response.body()
                    Result.failure(Exception(errorResponse.message))
                }

                HttpStatusCode.Unauthorized -> {
                    val errorResponse: ErrorResponse = response.body()
                    Result.failure(Exception(errorResponse.message))
                }

                HttpStatusCode.Forbidden -> {
                    val errorResponse: ErrorResponse = response.body()
                    Result.failure(Exception(errorResponse.message))
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
