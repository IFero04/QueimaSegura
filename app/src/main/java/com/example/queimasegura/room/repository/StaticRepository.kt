package com.example.queimasegura.room.repository

import com.example.queimasegura.room.dao.ControllerDao
import com.example.queimasegura.room.dao.ReasonDao
import com.example.queimasegura.room.dao.TypeDao
import com.example.queimasegura.room.entities.Controller
import com.example.queimasegura.room.entities.Reason
import com.example.queimasegura.room.entities.Type


data class StaticRepository(
    private val controllerDao: ControllerDao,
    private val reasonDao: ReasonDao,
    private val typeDao: TypeDao
) {
    // CONTROLLER
    suspend fun getController(): Controller? {
        return controllerDao.getController()
    }

    suspend fun addController(controller: Controller) {
        controllerDao.addController(controller)
    }

    suspend fun clearController() {
        controllerDao.clearController()
    }

    // REASONS
    suspend fun getReasons(): List<Reason>? {
        return reasonDao.getReasons()
    }

    suspend fun addReason(reason: Reason) {
        reasonDao.addReason(reason)
    }

    suspend fun clearReasons() {
        reasonDao.clearReasons()
    }

    // TYPES
    suspend fun getTypes(): List<Type>? {
        return typeDao.getTypes()
    }

    suspend fun addType(type: Type) {
        typeDao.addType(type)
    }

    suspend fun clearTypes() {
        typeDao.clearTypes()
    }
}


