package com.example.queimasegura.retrofit.api

import com.example.queimasegura.retrofit.model.*
import retrofit2.http.*
import retrofit2.Response


interface ApiService {
    @GET("/")
    suspend fun getRoot(): Response<Root>

    // USERS
    @POST("/users/")
    suspend fun createUser(
        @Body createUserSend: CreateUserSend
    ): Response<CreateUserGet>

    @POST("/users/check_email/")
    suspend fun checkEmail(
        @Query("email") email: String
    ): Response<CheckEmailGet>

    @POST("/login/")
    suspend fun loginUser(
        @Body loginSend: LoginSend
    ): Response<LoginGet>



    @GET("/location/")
    suspend fun getLocation(
        @Query("search") search: String
    ): Response<Location>
}