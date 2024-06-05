package com.example.queimasegura.retrofit.model

data class ResultData(
    val sessionId: String,
    val user: User
)

data class LoginGet(
    val status: String,
    val message: String,
    val result: ResultData,
)
