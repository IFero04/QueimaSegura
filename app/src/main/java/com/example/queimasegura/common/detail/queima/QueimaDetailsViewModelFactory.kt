package com.example.queimasegura.common.detail.queima

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.retrofit.repository.Repository

class QueimaDetailsViewModelFactory(
    private val application: Application,
    private val repository: Repository
) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QueimaDetailsViewModel(application, repository) as T
    }
}