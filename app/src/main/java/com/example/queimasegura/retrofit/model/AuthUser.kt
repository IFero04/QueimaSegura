package com.example.queimasegura.retrofit.model

import com.google.gson.annotations.SerializedName

data class AuthUserData(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("session_id")
    val sessionId: String,
)

data class AuthUser(
    val status: String,
    val message: String,
    val result: AuthUserData,
)
