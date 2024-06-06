package com.example.queimasegura.retrofit.model

data class CreateUserSend(
    val fullName: String,
    val email: String,
    var password: String,
    val nif: String
)
