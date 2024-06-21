package com.example.queimasegura.common.reqPerm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.retrofit.repository.Repository

class RequestViewModelFactory(
    private val application: Application,
    private val repository: Repository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RequestViewModel(application, repository) as T
    }
}