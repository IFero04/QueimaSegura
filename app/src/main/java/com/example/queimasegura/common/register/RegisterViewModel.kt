package com.example.queimasegura.common.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.model.CreateUserGet
import com.example.queimasegura.retrofit.model.CreateUserSend
import com.example.queimasegura.retrofit.model.SimpleResponseGet
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Auth
import com.example.queimasegura.room.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(
    application: Application,
    private val repository: Repository
): ViewModel() {
    private val _checkEmailResponse = MutableLiveData<Response<SimpleResponseGet>>()
    val checkEmailResponse: LiveData<Response<SimpleResponseGet>> get() = _checkEmailResponse
    private val _createUserResponse = MutableLiveData<Response<CreateUserGet>>()
    val createUserResponse: LiveData<Response<CreateUserGet>> get() = _createUserResponse

    private val authRepository: AuthRepository

    init {
        val authDao = AppDataBase.getDatabase(application).authDao()
        authRepository = AuthRepository(authDao)
    }

    fun checkEmail(
        email: String,
    ) {
        viewModelScope.launch {
            val response = repository.checkEmail(email)
            _checkEmailResponse.value = response
        }
    }

    fun createUser(
        createUserBody: CreateUserSend
    ) {
        viewModelScope.launch {
            val response = repository.createUser(createUserBody)
            _createUserResponse.value = response
            if(response.isSuccessful) {
                val resetJob = resetUserData()
                resetJob.join()
                response.body()?.result?.let {
                    val auth = Auth(
                        id = it.userId,
                        sessionId = it.sessionId,
                        email = createUserBody.email,
                        fullName = createUserBody.fullName,
                        nif = createUserBody.nif.toInt(),
                        type = 0
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