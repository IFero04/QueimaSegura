package com.example.queimasegura.retrofit.repository

import com.example.queimasegura.retrofit.model.*
import com.example.queimasegura.retrofit.api.RetrofitInstance
import retrofit2.Response


class Repository {

    suspend fun getRoot(): Response<Root> {
        return RetrofitInstance.api.getRoot()
    }
}