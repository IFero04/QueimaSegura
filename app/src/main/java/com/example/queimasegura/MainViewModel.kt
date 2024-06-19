package com.example.queimasegura

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.repository.AuthRepository
import com.example.queimasegura.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val application: Application,
    private val retrofitRepository: Repository
) : ViewModel() {
    private val authRepository: AuthRepository

    init {
        val userDao = AppDataBase.getDatabase(application).userDao()
        authRepository = AuthRepository(userDao)
    }

    fun startApp() {
        viewModelScope.launch(Dispatchers.IO) {
            val auth = authRepository.getAuth()
            val isInternetAvailable = NetworkUtils.isInternetAvailable(application)
            if (auth != null) {
                // Handle case when user is authenticated
                Log.d("Auth", "User is authenticated")
            } else {
                // Handle case when user is not authenticated
                Log.d("Auth", "User is not authenticated")
            }

            if (isInternetAvailable) {
                Log.d("Network", "Internet is available")
            } else {
                Log.d("Network", "Internet is not available")
            }
        }
    }
}
