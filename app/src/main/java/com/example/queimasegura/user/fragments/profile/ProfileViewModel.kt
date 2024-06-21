package com.example.queimasegura.user.fragments.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Auth
import com.example.queimasegura.room.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val application: Application,
    private val retrofitRepository: Repository
): ViewModel() {
    val authData: LiveData<Auth>

    private val authRepository: AuthRepository

    init {
        val authDao = AppDataBase.getDatabase(application).authDao()
        authRepository = AuthRepository(authDao)
        authData = authRepository.readData

    }

    fun logoutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.delAuth()
        }
    }

}