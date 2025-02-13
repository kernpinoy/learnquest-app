package org.cutie.learnquest.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val username: String,
    val password: String
)