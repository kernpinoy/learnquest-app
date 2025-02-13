package org.cutie.learnquest.data.api

sealed class Routes(val route: String) {
    companion object {
//        const val BASE_URL = "http://10.0.0.4:3030"
        const val BASE_URL = "https://mobile.jacobsky.me"
    }

    object Login : Routes("/login")
    object Register : Routes("/register")
    object Logout : Routes("/logout")
    object Charts : Routes("/files/charts")

    fun fullUrl() = BASE_URL + route
}