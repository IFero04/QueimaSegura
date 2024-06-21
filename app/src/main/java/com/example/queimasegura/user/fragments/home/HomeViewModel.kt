package com.example.queimasegura.user.fragments.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Status
import com.example.queimasegura.room.repository.AuthRepository
import com.example.queimasegura.room.repository.StatusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel (
    application: Application,
    private val repository: Repository
): ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username
    val statusData: LiveData<Status>

    private val authRepository: AuthRepository
    private val statusRepository: StatusRepository

    init {
        val authDao = AppDataBase.getDatabase(application).authDao()
        authRepository = AuthRepository(authDao)
        val statusDao = AppDataBase.getDatabase(application).statusDao()
        statusRepository = StatusRepository(statusDao)
        statusData = statusRepository.readData
    }

    fun fetchUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            val auth = authRepository.getAuth()
            if (auth != null) {
                _username.postValue(auth.fullName)
            }
        }
    }

    fun fetchUserStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            statusRepository.clearStatus()
            statusRepository.addStatus(Status(id= 0, firesPending = 1, firesComplete = 5))
        }
    }
}