package com.example.queimasegura.retrofit.api
import retrofit2.Call
import retrofit2.http.*
import com.example.queimasegura.retrofit.model.Root


interface ApiService {
    @GET("/")
    fun getApiName(): Call<Root>
}