package com.example.queimasegura.common.detail.queima

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Auth
import com.example.queimasegura.room.repository.AuthRepository

class QueimaDetailsViewModel (
    private val application: Application,
    private val retrofitRepository: Repository
) : AndroidViewModel(application)  {
    val authData: LiveData<Auth>
    private lateinit var authUser: Auth

    private val authRepository: AuthRepository

    init {
        val database = AppDataBase.getDatabase(application)
        authRepository = AuthRepository(database.authDao())
        authData = authRepository.readData

        observeAuthData()
    }

    private fun observeAuthData() {
        authData.observeForever { auth ->
            auth?.let {
                authUser = it
            }
        }
    }
}