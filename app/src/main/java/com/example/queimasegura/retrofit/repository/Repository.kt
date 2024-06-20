package com.example.queimasegura.retrofit.repository

import com.example.queimasegura.retrofit.model.*
import com.example.queimasegura.retrofit.api.RetrofitInstance
import com.example.queimasegura.retrofit.util.MD5
import retrofit2.Response


class Repository {

    suspend fun getRoot(): Response<Root> {
        return RetrofitInstance.api.getRoot()
    }

    // STATIC
    suspend fun getTypes(): Response<TypesGet> {
        return RetrofitInstance.api.getTypes()
    }

    suspend fun getReasons(): Response<ReasonsGet> {
        return RetrofitInstance.api.getReasons()
    }

    suspend fun getController(): Response<ControllerGet> {
        return RetrofitInstance.api.getController()
    }

    // AUTH
    suspend fun checkEmail(
        email: String
    ): Response<SimpleResponseGet> {
        return RetrofitInstance.api.checkEmail(email)
    }

    suspend fun checkSession(
        userId: String,
        sessionId: String
    ): Response<SimpleResponseGet> {
        return RetrofitInstance.api.checkSession(userId, sessionId)
    }

    suspend fun loginUser(
        loginSend: LoginSend
    ): Response<LoginGet>{
        loginSend.password = MD5().getMD5Hash(loginSend.password)
        return RetrofitInstance.api.loginUser(loginSend)
    }

    suspend fun logoutUser(
        userId: String,
        sessionId: String
    ): Response<SimpleResponseGet> {
        return RetrofitInstance.api.logoutUser(userId, sessionId)
    }


    // USERS
    suspend fun createUser(
        createUserSend: CreateUserSend
    ): Response<CreateUserGet> {
        createUserSend.password = MD5().getMD5Hash(createUserSend.password)
        return RetrofitInstance.api.createUser(createUserSend)
    }

    suspend fun getLocation(search: String): Response<Location> {
        return RetrofitInstance.api.getLocation(search)
    }
}