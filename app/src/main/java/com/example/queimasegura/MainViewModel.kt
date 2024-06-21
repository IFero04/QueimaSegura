package com.example.queimasegura

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Auth
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
        INTRO, HOME, LOGIN, ERROR
    }
    private val _appState = MutableLiveData<AppState>()
    val appState: LiveData<AppState> get() = _appState
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    var isAppStarted: Boolean = false

    val authData: LiveData<Auth>
    private var currentAuth: Auth? = null

    private val authRepository: AuthRepository
    private val staticRepository: StaticRepository

    init {
        val database = AppDataBase.getDatabase(application)
        authRepository = AuthRepository(database.authDao())
        staticRepository = StaticRepository(
            database.controllerDao(),
            database.reasonDao(),
            database.typeDao()
        )
        authData = authRepository.readData
    }


    fun startApp() {
        isAppStarted = true
        viewModelScope.launch(Dispatchers.IO) {
            val isInternetAvailable = NetworkUtils.isInternetAvailable(application)
            val auth = authData.value
            val controller = staticRepository.getController()

            handleInternetAvailability(isInternetAvailable, auth, controller)
        }
    }

    fun firstRun() {
        isAppStarted = true
        viewModelScope.launch(Dispatchers.IO) {
            val isInternetAvailable = NetworkUtils.isInternetAvailable(application)
            val auth = authData.value
            val controller = staticRepository.getController()

            if(isInternetAvailable) {
                handleController(controller)
                _appState.postValue(AppState.INTRO)
            } else {
                handleOfflineMode(auth, controller)
            }
        }
    }

    private suspend fun handleInternetAvailability(
        isInternetAvailable: Boolean,
        auth: Auth?,
        controller: Controller?
    ) {
        if(isInternetAvailable) {
            handleController(controller)
            handleAuth(auth)
        } else {
            handleOfflineMode(auth, controller)
        }
    }

    private suspend fun handleController(controller: Controller?) {
        if (controller == null) {
            updateStaticData()
        } else {
            checkController(controller) { isSameController ->
                if (!isSameController) {
                    updateStaticData()
                }
            }
        }
    }

    private suspend fun handleAuth(auth: Auth?) {
        if (auth == null) {
            _appState.postValue(AppState.LOGIN)
        } else {
            checkSession(auth.id, auth.sessionId) { isSameSession ->
                if (isSameSession) {
                    _appState.postValue(AppState.HOME)
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        deleteAuthAndRedirectToLogin()
                    }
                }
            }
        }
    }

    private suspend fun deleteAuthAndRedirectToLogin() {
        authRepository.delAuth()
        _appState.postValue(AppState.LOGIN)
        _errorMessage.postValue(application.getString(R.string.main_error_login))
    }

    private fun handleOfflineMode(auth: Auth?, controller: Controller?) {
        if (controller == null) {
            _appState.postValue(AppState.ERROR)
            _errorMessage.postValue(application.getString(R.string.main_error_controller))
        } else if (auth == null) {
            _appState.postValue(AppState.ERROR)
            _errorMessage.postValue(application.getString(R.string.main_error_auth))
        } else {
            _appState.postValue(AppState.HOME)
        }
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
