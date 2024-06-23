package com.example.queimasegura.common.fire.map

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.R
import com.example.queimasegura.retrofit.model.data.Location
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.util.ApiUtils
import kotlinx.coroutines.launch
import retrofit2.Response


class MapViewModel(
    private val application: Application,
    private val retrofitRepository: Repository
) : ViewModel() {
    fun getMapLocation(lat: Double, lng: Double, handleSendLocation: (Location) -> Unit) {
        viewModelScope.launch {
            val response = retrofitRepository.getMapLocation(lat, lng)
            if(response.isSuccessful) {
                response.body()?.result.let { locations ->
                    locations?.get(0)?.let { handleSendLocation(it) }
                }
            } else if(response.errorBody() != null) {
                ApiUtils.handleApiError(application, response.errorBody(), ::showMessage)
            } else{
                showMessage(application.getString(R.string.server_error))
            }
        }
    }

    private fun showMessage(str: String) {
        Toast.makeText(application, str, Toast.LENGTH_LONG).show()
    }
}