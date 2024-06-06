package com.example.queimasegura.retrofit.model

data class CreateUser(
    val fullName: String,
    val email: String,
    val password: String,
    val nif: String,
)
