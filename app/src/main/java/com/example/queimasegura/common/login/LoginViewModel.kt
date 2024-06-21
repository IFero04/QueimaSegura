package com.example.queimasegura.common.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.model.get.Login
import com.example.queimasegura.retrofit.model.send.LoginBody
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Auth
import com.example.queimasegura.room.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class LoginViewModel(
    private val application: Application,
    private val retrofitRepository: Repository
): ViewModel() {
    private val _loginResponse = MutableLiveData<Response<Login>>()
    val loginResponse: LiveData<Response<Login>> get() = _loginResponse

    private val authRepository: AuthRepository

    init {
        val authDao = AppDataBase.getDatabase(application).authDao()
        authRepository = AuthRepository(authDao)
    }

    fun loginUser(loginBody: LoginBody) {
        viewModelScope.launch {
            val response = retrofitRepository.loginUser(loginBody)
            _loginResponse.value = response
            if(response.isSuccessful) {
                val resetJob = resetUserData()
                resetJob.join()
                response.body()?.result?.let {
                    val auth = Auth(
                        id = it.user.id,
                        sessionId = it.sessionId,
                        email = loginBody.email,
                        fullName = it.user.fullName,
                        nif = it.user.nif,
                        type = it.user.type
                    )
                    saveUser(auth)
                }
            }
        }
    }

    private fun saveUser(auth: Auth) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.authenticate(auth)
        }
    }

    private fun resetUserData() = viewModelScope.launch(Dispatchers.IO) {
        authRepository.delAuth()
    }
}