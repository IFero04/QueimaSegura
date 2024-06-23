package com.example.queimasegura.common.fire

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.model.data.Location
import com.example.queimasegura.retrofit.model.get.CreateFire
import com.example.queimasegura.retrofit.model.send.CreateFireBody
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Auth
import com.example.queimasegura.room.entities.Reason
import com.example.queimasegura.room.entities.Type
import com.example.queimasegura.room.repository.AuthRepository
import com.example.queimasegura.room.repository.StaticRepository
import com.example.queimasegura.util.NetworkUtils
import kotlinx.coroutines.launch
import retrofit2.Response


class CreateFireViewModel(
    private val application: Application,
    private val repository: Repository
) : ViewModel() {
    private val _createFireResponse = MutableLiveData<Response<CreateFire>>()
    val createFireResponse: LiveData<Response<CreateFire>> get () = _createFireResponse

    val typesData: LiveData<List<Type>>
    val reasonsData: LiveData<List<Reason>>
    val authData: LiveData<Auth>
    private lateinit var authUser: Auth

    private val staticRepository: StaticRepository
    private val authRepository: AuthRepository

    init {
        val database = AppDataBase.getDatabase(application)
        authRepository = AuthRepository(database.authDao())
        staticRepository = StaticRepository(
            database.controllerDao(), database.reasonDao(), database.typeDao()
        )
        authData = authRepository.readData
        typesData = staticRepository.readTypesData
        reasonsData = staticRepository.readReasonsData

        observeAuth()
    }

    private fun observeAuth() {
        authRepository.readData.observeForever { auth ->
            auth?.let {
                Log.d("AUTH", it.toString())
                authUser = it
            }
        }
    }

    fun createFire(
        createFireBody: CreateFireBody
    ) {
        val isInternetAvailable = NetworkUtils.isInternetAvailable(application)
        if(isInternetAvailable) {
            if(::authUser.isInitialized) {
                viewModelScope.launch {
                    val response = repository.createFire(authUser.id, authUser.sessionId, createFireBody)
                    Log.d("RESPONSE", response.toString())
                    _createFireResponse.value = response
                    if(response.isSuccessful) {
                        TODO()
                    }
                }
            }
        }
    }
}