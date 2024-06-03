package com.example.queimasegura.retrofit.api

import com.example.queimasegura.retrofit.model.*
import retrofit2.http.*


interface ApiService {
    @GET("/")
    suspend fun getRoot(): Root
}