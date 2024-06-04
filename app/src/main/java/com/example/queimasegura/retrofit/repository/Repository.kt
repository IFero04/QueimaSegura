package com.example.queimasegura.retrofit.repository

import android.icu.text.StringSearch
import com.example.queimasegura.retrofit.model.*
import com.example.queimasegura.retrofit.api.RetrofitInstance
import retrofit2.Response


class Repository {

    suspend fun getRoot(): Response<Root> {
        return RetrofitInstance.api.getRoot()
    }

    suspend fun loginUser(login: Login): Response<AuthUser>{
        return RetrofitInstance.api.loginUser(login)
    }

    suspend fun getLocation(search: String): Response<Location> {
        return RetrofitInstance.api.getLocation(search)
    }
}