package com.example.queimasegura.common.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.model.AuthUser
import com.example.queimasegura.retrofit.model.Login
import com.example.queimasegura.retrofit.model.Root
import com.example.queimasegura.retrofit.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val repository: Repository): ViewModel() {
    val loginResponse: MutableLiveData<Response<AuthUser>> = MutableLiveData()

    fun loginUser(login: Login) {
        viewModelScope.launch {
            val response = repository.loginUser(login)
            loginResponse.value = response
        }
    }
}