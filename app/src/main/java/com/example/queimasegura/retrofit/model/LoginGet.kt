package com.example.queimasegura.retrofit.model

data class LoginData(
    val sessionId: String,
    val user: User
)

data class LoginGet(
    val status: String,
    val message: String,
    val result: LoginData,
)
