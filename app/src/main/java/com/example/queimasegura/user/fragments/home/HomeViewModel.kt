package com.example.queimasegura.user.fragments.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Status
import com.example.queimasegura.room.repository.AuthRepository
import com.example.queimasegura.room.repository.StatusRepository
import com.example.queimasegura.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel (
    private val application: Application,
    private val retrofitRepository: Repository
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
            val isInternetAvailable = NetworkUtils.isInternetAvailable(application)
            if(isInternetAvailable) {
                val auth = authRepository.getAuth()
                if(auth != null){
                    val response = retrofitRepository.getUserStatus(auth.id, auth.sessionId)
                    if(response.isSuccessful) {
                        response.body()?.let { userStatus ->
                            val status = Status(
                                id = 0,
                                firesPending = userStatus.result.firesPending,
                                firesComplete = userStatus.result.firesComplete
                            )
                            statusRepository.clearStatus()
                            statusRepository.addStatus(status)
                        }
                    }
                }
            }
        }
    }
}