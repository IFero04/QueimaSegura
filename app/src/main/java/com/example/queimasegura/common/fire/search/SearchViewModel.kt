package com.example.queimasegura.common.fire.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.model.get.Location
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Auth
import com.example.queimasegura.room.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response


class SearchViewModel(
    private val application: Application,
    private val retrofitRepository: Repository
): ViewModel() {
    private val _locationResponse = MutableLiveData<Response<Location>>()
    val locationResponse: LiveData<Response<Location>> get() = _locationResponse

    private val authData: LiveData<Auth>
    private lateinit var authUser: Auth

    private val authRepository: AuthRepository

    init {
        val database = AppDataBase.getDatabase(application)
        authRepository = AuthRepository(database.authDao())
        authData = authRepository.readData

        observeAuth()
    }

    private fun observeAuth() {
        authRepository.readData.observeForever { auth ->
            auth?.let {
                authUser = it
            }
        }
    }

    fun getLocation(search: String) {
        viewModelScope.launch {
            if(::authUser.isInitialized) {
                val response = retrofitRepository.getLocation(authUser.id, authUser.sessionId, search)
                _locationResponse.value = response
            }
        }
    }

}