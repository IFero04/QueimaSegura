package com.example.queimasegura.retrofit.model

data class CreateUserData(
    val sessionId: String,
    val userId: String
)

data class CreateUserGet(
    val status: String,
    val message: String,
    val result: CreateUserData,
)
