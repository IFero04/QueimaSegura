package com.example.queimasegura.admin.fragments.home.fire.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.retrofit.repository.AdminRepository
import com.example.queimasegura.retrofit.repository.Repository

class SearchUserViewModelFactory(
    private val application: Application,
    private val adminRepository: AdminRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchUserViewModel(application, adminRepository) as T
    }
}