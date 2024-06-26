package com.example.queimasegura.retrofit.model.get.admin

import com.example.queimasegura.retrofit.model.data.admin.AdminStatus


data class AdminStatus(
    val status: String,
    val message: String,
    val result: AdminStatus
)
