package com.example.queimasegura.common.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queimasegura.retrofit.model.Root
import com.example.queimasegura.retrofit.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository): ViewModel() {
    val myResponse: MutableLiveData<Root> = MutableLiveData()

    fun getRoot() {
        viewModelScope.launch {
            val response = repository.getRoot()
            myResponse.value = response
        }
    }
}