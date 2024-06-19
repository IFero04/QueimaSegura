package com.example.queimasegura.room.repository

import com.example.queimasegura.room.dao.AuthDao
import com.example.queimasegura.room.entities.Auth

class AuthRepository(private val authDao: AuthDao) {
    suspend fun getAuth(): Auth? {
        return authDao.readAuthData()
    }

    suspend fun authenticate(auth: Auth){
        authDao.auth(auth)
    }

    suspend fun delAuth(){
        authDao.deleteAll()
    }
}