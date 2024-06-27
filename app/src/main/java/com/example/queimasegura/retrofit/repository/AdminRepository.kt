package com.example.queimasegura.retrofit.repository

import com.example.queimasegura.retrofit.api.RetrofitInstance
import com.example.queimasegura.retrofit.model.Root
import com.example.queimasegura.retrofit.model.admin.get.AdminGetUsers
import com.example.queimasegura.retrofit.model.admin.get.AdminStatus
import com.example.queimasegura.retrofit.model.get.SimpleResponse
import retrofit2.Response


class AdminRepository {
    suspend fun getRoot(): Response<Root> {
        return RetrofitInstance.adminApi.getRoot()
    }

    suspend fun adminGetStatus(
        adminId: String,
        sessionId: String,
    ): Response<AdminStatus>  {
        return RetrofitInstance.adminApi.adminGetStatus(adminId, sessionId)
    }

    suspend fun adminGetUsers(
        adminId: String,
        sessionId: String,
    ): Response<AdminGetUsers>  {
        return RetrofitInstance.adminApi.adminGetUsers(adminId, sessionId)
    }

    suspend fun banUser(
        userId: String,
        adminId: String,
        sessionId: String
    ): Response<SimpleResponse>  {
        return RetrofitInstance.adminApi.banUser(userId, adminId, sessionId)
    }

    suspend fun unbanUser(
        userId: String,
        adminId: String,
        sessionId: String
    ): Response<SimpleResponse>  {
        return RetrofitInstance.adminApi.unbanUser(userId, adminId, sessionId)
    }

    suspend fun deleteUser(
        userId: String,
        adminId: String,
        sessionId: String
    ): Response<SimpleResponse>  {
        return RetrofitInstance.adminApi.deleteUser(userId, adminId, sessionId)
    }

    suspend fun restoreUser(
        userId: String,
        adminId: String,
        sessionId: String
    ): Response<SimpleResponse>  {
        return RetrofitInstance.adminApi.restoreUser(userId, adminId, sessionId)
    }
}