package com.example.queimasegura.retrofit.repository

import com.example.queimasegura.retrofit.model.*
import com.example.queimasegura.retrofit.api.RetrofitInstance
import com.example.queimasegura.retrofit.util.MD5
import retrofit2.Response


class Repository {

    suspend fun getRoot(): Response<Root> {
        return RetrofitInstance.api.getRoot()
    }

    suspend fun createUser(
        createUserSend: CreateUserSend
    ): Response<CreateUserGet> {
        createUserSend.password = MD5().getMD5Hash(createUserSend.password)
        return RetrofitInstance.api.createUser(createUserSend)
    }

    suspend fun checkEmail(
        email: String
    ): Response<CheckEmailGet> {
        return RetrofitInstance.api.checkEmail(email)
    }

    suspend fun loginUser(loginSend: LoginSend): Response<LoginGet>{
        loginSend.password = MD5().getMD5Hash(loginSend.password)
        return RetrofitInstance.api.loginUser(loginSend)
    }

    suspend fun getLocation(search: String): Response<Location> {
        return RetrofitInstance.api.getLocation(search)
    }
}