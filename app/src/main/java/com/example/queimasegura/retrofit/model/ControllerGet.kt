package com.example.queimasegura.retrofit.model


data class ControllerData(
    val id: Int,
    val name: String
)

data class ControllerGet(
    val status: String,
    val message: String,
    val result: ControllerData,
)
