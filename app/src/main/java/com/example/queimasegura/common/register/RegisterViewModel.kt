package com.example.queimasegura.common.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.model.CheckEmailGet
import com.example.queimasegura.retrofit.model.CreateUserGet
import com.example.queimasegura.retrofit.model.CreateUserSend
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.User
import com.example.queimasegura.room.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(
    application: Application,
    private val repository: Repository
): ViewModel() {
    private val _checkEmailResponse = MutableLiveData<Response<CheckEmailGet>>()
    val checkEmailResponse: LiveData<Response<CheckEmailGet>> get() = _checkEmailResponse
    private val _createUserResponse = MutableLiveData<Response<CreateUserGet>>()
    val createUserResponse: LiveData<Response<CreateUserGet>> get() = _createUserResponse

    private val userRepository: UserRepository

    init {
        val userDao = AppDataBase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
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
                    val user = User(
                        id = it.userId,
                        sessionId = it.sessionId,
                        email = createUserBody.email,
                        fullName = createUserBody.fullName,
                        nif = createUserBody.nif.toInt(),
                        type = 0
                    )
                    saveUser(user)
                }
            }
        }
    }

    private fun saveUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(user)
        }
    }

    private fun resetUserData() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.deleteAll()
    }
}