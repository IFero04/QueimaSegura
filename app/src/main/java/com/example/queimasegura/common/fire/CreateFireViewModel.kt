package com.example.queimasegura.common.fire

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.queimasegura.retrofit.model.data.Location
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.db.AppDataBase
import com.example.queimasegura.room.entities.Reason
import com.example.queimasegura.room.entities.Type
import com.example.queimasegura.room.repository.StaticRepository


class CreateFireViewModel(
    private val application: Application,
    private val repository: Repository
) : ViewModel() {
    val typesData: LiveData<List<Type>>
    val reasonsData: LiveData<List<Reason>>

    private val staticRepository: StaticRepository

    init {
        val database = AppDataBase.getDatabase(application)
        staticRepository = StaticRepository(
            database.controllerDao(), database.reasonDao(), database.typeDao()
        )
        typesData = staticRepository.readTypesData
        reasonsData = staticRepository.readReasonsData
    }
}