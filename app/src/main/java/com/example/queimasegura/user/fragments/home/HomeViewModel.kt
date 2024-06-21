package com.example.queimasegura.user.fragments.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel (
    application: Application,
    private val repository: Repository
): ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val authRepository: AuthRepository

    init {
        val authDao = AppDataBase.getDatabase(application).authDao()
        authRepository = AuthRepository(authDao)
    }

    fun fetchUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            val auth = authRepository.getAuth()
            if (auth != null) {
                _username.postValue(auth.fullName)
            }
        }
    }
}