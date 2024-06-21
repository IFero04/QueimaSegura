package com.example.queimasegura.user.fragments.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Auth
import com.example.queimasegura.room.entities.Status
import com.example.queimasegura.room.repository.AuthRepository
import com.example.queimasegura.room.repository.StatusRepository
import com.example.queimasegura.util.ApiUtils
import com.example.queimasegura.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel (
    private val application: Application,
    private val retrofitRepository: Repository
) : AndroidViewModel(application) {
    val statusData: LiveData<Status>
    val authData: LiveData<Auth>

    private val authRepository: AuthRepository
    private val statusRepository: StatusRepository

    init {
        val authDao = AppDataBase.getDatabase(application).authDao()
        authRepository = AuthRepository(authDao)
        authData = authRepository.readData

        val statusDao = AppDataBase.getDatabase(application).statusDao()
        statusRepository = StatusRepository(statusDao)
        statusData = statusRepository.readData

        observeAuthData()
    }

    private fun observeAuthData() {
        authData.observeForever { auth ->
            auth?.let {
                fetchUserStatus(it)
            }
        }
    }

    private fun fetchUserStatus(auth: Auth) {
        viewModelScope.launch(Dispatchers.IO) {
            val isInternetAvailable = NetworkUtils.isInternetAvailable(getApplication())
            if (isInternetAvailable) {
                val response = retrofitRepository.getUserStatus(auth.id, auth.sessionId)
                if (response.isSuccessful) {
                    response.body()?.let { userStatus ->
                        val status = Status(
                            id = 0,
                            firesPending = userStatus.result.firesPending + 2,
                            firesComplete = userStatus.result.firesComplete + 3
                        )
                        statusRepository.clearStatus()
                        statusRepository.addStatus(status)
                    }
                } else if(response.errorBody() != null) {
                    ApiUtils.handleApiError(application, response.errorBody(), ::showMessage)
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(application, message, Toast.LENGTH_LONG).show()
    }
}
