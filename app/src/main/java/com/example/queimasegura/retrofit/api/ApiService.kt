package com.example.queimasegura.retrofit.api

import com.example.queimasegura.retrofit.model.*
import retrofit2.http.*
import retrofit2.Response


interface ApiService {
    @GET("/")
    suspend fun getRoot(): Response<Root>

    // STATIC
    @GET("/static/types")
    suspend fun getTypes(): Response<TypesGet>

    @GET("/static/reasons")
    suspend fun getReasons(): Response<ReasonsGet>

    @GET("/static/controller")
    suspend fun getController(): Response<ControllerGet>

    // AUTH
    @GET("/auth/check_email/")
    suspend fun checkEmail(
        @Query("email") email: String
    ): Response<SimpleResponseGet>

    @GET("/auth/check_session")
    suspend fun checkSession(
        @Query("user_id") userId: String,
        @Query("session_id") sessionId: String,
    ): Response<SimpleResponseGet>

    @POST("/auth/login/")
    suspend fun loginUser(
        @Body loginSend: LoginSend
    ): Response<LoginGet>

    @DELETE("/auth/logout")
    suspend fun logoutUser(
        @Query("user_id") userId: String,
        @Query("session_id") sessionId: String,
    ): Response<SimpleResponseGet>


    // USERS
    @POST("/users/create")
    suspend fun createUser(
        @Body createUserSend: CreateUserSend
    ): Response<CreateUserGet>

    @GET("/location/")
    suspend fun getLocation(
        @Query("search") search: String
    ): Response<Location>
}