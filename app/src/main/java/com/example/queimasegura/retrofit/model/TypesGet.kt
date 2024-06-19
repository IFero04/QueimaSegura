package com.example.queimasegura.retrofit.model


data class TypesData(
    val id: Int,
    val namePt: String,
    val nameEn: String
)

data class TypesGet(
    val status: String,
    val message: String,
    val result: List<TypesData>,
)
