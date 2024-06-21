package com.example.queimasegura

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Controller
import com.example.queimasegura.room.entities.Type
import com.example.queimasegura.room.entities.Reason
import com.example.queimasegura.room.repository.AuthRepository
import com.example.queimasegura.room.repository.StaticRepository
import com.example.queimasegura.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val application: Application,
    private val retrofitRepository: Repository
) : ViewModel() {
    enum class AppState {
        HOME, LOGIN, ERROR
    }
    private val _appState = MutableLiveData<AppState>()
    val appState: LiveData<AppState> get() = _appState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val authRepository: AuthRepository
    private val staticRepository: StaticRepository

    init {
        val authDao = AppDataBase.getDatabase(application).authDao()
        authRepository = AuthRepository(authDao)

        val controllerDao = AppDataBase.getDatabase(application).controllerDao()
        val reasonDao = AppDataBase.getDatabase(application).reasonDao()
        val typeDao = AppDataBase.getDatabase(application).typeDao()
        staticRepository = StaticRepository(controllerDao, reasonDao, typeDao)
    }

    fun startApp() {
        viewModelScope.launch(Dispatchers.IO) {
            val isInternetAvailable = NetworkUtils.isInternetAvailable(application)
            val auth = authRepository.getAuth()
            val controller = staticRepository.getController()

            if(isInternetAvailable) {
                if (controller == null) {
                    updateStaticData()
                } else {
                    checkController(controller) { isSameController ->
                        if (!isSameController) {
                            updateStaticData()
                        }
                    }
                }
                if (auth == null) {
                    _appState.postValue(AppState.LOGIN)
                } else {
                    checkSession(auth.id, auth.sessionId) { isSameSession ->
                        if (isSameSession) {
                            _appState.postValue(AppState.HOME)
                        } else {
                            viewModelScope.launch(Dispatchers.IO) {
                                authRepository.delAuth()
                                _appState.postValue(AppState.LOGIN)
                                _errorMessage.postValue(application.getString(R.string.main_error_login))
                            }
                        }
                    }
                }
            } else {
                if (controller == null) {
                    _appState.postValue(AppState.ERROR)
                    _errorMessage.postValue(application.getString(R.string.main_error_controller))
                } else {
                    if (auth == null) {
                        _appState.postValue(AppState.ERROR)
                        _errorMessage.postValue(application.getString(R.string.main_error_auth))
                    } else {
                        _appState.postValue(AppState.HOME)
                    }
                }
            }
        }
    }

    fun firstRun() {
        updateStaticData()
    }

    private fun checkController(
        roomController: Controller,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val controllerResponse = retrofitRepository.getController()
            if(controllerResponse.isSuccessful) {
                val controllerData = controllerResponse.body()?.result
                if (controllerData != null && controllerData.id == roomController.id && controllerData.name == roomController.name) {
                    callback(true)
                } else{
                    callback(false)
                }
            } else {
                callback(false)
            }
        }
    }

    private fun checkSession(
        userId: String,
        sessionId: String,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val sessionResponse = retrofitRepository.checkSession(userId, sessionId)
            if(sessionResponse.isSuccessful) {
                callback(true)
            } else{
                callback(false)
            }
        }
    }

    private fun updateStaticData() {
        viewModelScope.launch(Dispatchers.IO) {
            val controllerResponse = retrofitRepository.getController()
            if(controllerResponse.isSuccessful) {
                staticRepository.clearController()
                val controllerData = controllerResponse.body()?.result
                if (controllerData != null) {
                    staticRepository.addController(Controller(
                        id = controllerData.id,
                        name = controllerData.name
                    ))
                }
            }

            val typesResponse = retrofitRepository.getTypes()
            if(typesResponse.isSuccessful) {
                staticRepository.clearTypes()
                typesResponse.body()?.result?.forEach { typeData ->
                    staticRepository.addType(
                        Type(
                            id = typeData.id,
                            namePt = typeData.namePt,
                            nameEn = typeData.nameEn
                        )
                    )
                }
            }

            val reasonsResponse = retrofitRepository.getReasons()
            if(reasonsResponse.isSuccessful) {
                staticRepository.clearReasons()
                reasonsResponse.body()?.result?.forEach { reasonData ->
                    staticRepository.addReason(
                        Reason(
                            id = reasonData.id,
                            namePt = reasonData.namePt,
                            nameEn = reasonData.nameEn
                        )
                    )
                }
            }
        }
    }
}
