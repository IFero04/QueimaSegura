package com.example.queimasegura

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Auth
import com.example.queimasegura.room.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val retrofitRepository: Repository
): ViewModel() {
    private val authRepository: AuthRepository

    init {
        val userDao = AppDataBase.getDatabase(application).userDao()
        authRepository = AuthRepository(userDao)
    }

    fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            val auth = authRepository.getAuth()
            Log.d("USER", auth.toString())
        }
    }

}