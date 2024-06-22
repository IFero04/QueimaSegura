package com.example.queimasegura.common.fire

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.queimasegura.retrofit.repository.Repository

class CreateFireViewModel(
    private val application: Application,
    private val repository: Repository
): ViewModel() {
}