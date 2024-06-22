package com.example.queimasegura.common.fire.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.model.get.Location
import com.example.queimasegura.retrofit.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(
    private val application: Application,
    private val retrofitRepository: Repository
): ViewModel() {
    private val _locationResponse = MutableLiveData<Response<Location>>()
    val locationResponse: LiveData<Response<Location>> get() = _locationResponse

    fun getLocation(search: String) {
        viewModelScope.launch {
            val response = retrofitRepository.getLocation(search)
            _locationResponse.postValue(response)
        }
    }

}