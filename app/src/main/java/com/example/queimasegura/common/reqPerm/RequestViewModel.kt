package com.example.queimasegura.common.reqPerm

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.queimasegura.retrofit.repository.Repository

class RequestViewModel(
    private val application: Application,
    private val repository: Repository
): ViewModel() {
}