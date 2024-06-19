package com.example.queimasegura.retrofit.model


data class ReasonsData(
    val id: Int,
    val namePt: String,
    val nameEn: String
)

data class ReasonsGet(
    val status: String,
    val message: String,
    val result: List<ReasonsData>,
)
