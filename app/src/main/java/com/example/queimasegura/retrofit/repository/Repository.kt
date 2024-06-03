package com.example.queimasegura.retrofit.repository
import com.example.queimasegura.retrofit.api.RetrofitInstance
import com.example.queimasegura.retrofit.model.Root
import retrofit2.Call

class Repository {

    suspend fun getRoot(): Root {
        return RetrofitInstance.api.getRoot()
    }
}