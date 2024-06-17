package com.example.queimasegura.retrofit.api

import com.example.queimasegura.retrofit.model.*
import retrofit2.http.*
import retrofit2.Response


interface ApiService {
    @GET("/")
    suspend fun getRoot(): Response<Root>

    // AUTH
    @GET("/auth/check_email")
    suspend fun checkEmail(
        @Query("email") email: String
    ): Response<CheckEmailGet>

    @GET("/auth/check_session")
    suspend fun checkSession(
        @Query("user_id") userId: String,
        @Query("session_id") sessionId: String
    ): Response<CheckSessionGet>

    @POST("/login/")
    suspend fun loginUser(
        @Body loginSend: LoginSend
    ): Response<LoginGet>

    // USERS
    @POST("/users/")
    suspend fun createUser(
        @Body createUserSend: CreateUserSend
    ): Response<CreateUserGet>


    @GET("/location/")
    suspend fun getLocation(
        @Query("search") search: String
    ): Response<Location>
}