package org.cutie.learnquest.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class Register(
    val lrn: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val sex: String,
    val password: String,
    val classCode: String
)