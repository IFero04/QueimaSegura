package com.example.queimasegura

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    enum class UserState {
        INTERNET_AUTH, INTERNET_NO_AUTH, NO_INTERNET_AUTH, NO_INTERNET_NO_AUTH, UNKNOWN
    }
    private val _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> get() = _userState

    private val authRepository: AuthRepository

    init {
        val userDao = AppDataBase.getDatabase(application).userDao()
        authRepository = AuthRepository(userDao)
        _userState.value = UserState.UNKNOWN
    }

    fun startApp() {
        viewModelScope.launch(Dispatchers.IO) {
            val auth = authRepository.getAuth()
            val isInternetAvailable = NetworkUtils.isInternetAvailable(application)

            if (isInternetAvailable) {
                if (auth != null) {
                    val response = retrofitRepository.checkSession(auth.id, auth.sessionId)
                    if (response.isSuccessful) {
                        _userState.postValue(UserState.INTERNET_AUTH)
                    } else {
                        _userState.postValue(UserState.INTERNET_NO_AUTH)
                    }
                } else {
                    _userState.postValue(UserState.INTERNET_NO_AUTH)
                }
            } else {
                if (auth != null) {
                    _userState.postValue(UserState.NO_INTERNET_AUTH)
                } else {
                    _userState.postValue(UserState.NO_INTERNET_NO_AUTH)
                }
            }
        }
    }
}
