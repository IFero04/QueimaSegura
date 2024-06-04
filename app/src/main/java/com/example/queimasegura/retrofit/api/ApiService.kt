package com.example.queimasegura.retrofit.api

import com.example.queimasegura.retrofit.model.*
import retrofit2.http.*
import retrofit2.Response


interface ApiService {
    @GET("/")
    suspend fun getRoot(): Response<Root>

    // USERS
    @POST("/login/")
    suspend fun loginUser(
        @Body login: Login
    ): Response<AuthUser>

    @POST("/users/")
    suspend fun createUser(): Response<CreateUser>

    @GET("/location/")
    suspend fun getLocation(
        @Query("search") search: String
    ): Response<Location>
}