package com.example.queimasegura.room.repository

import androidx.lifecycle.LiveData
import com.example.queimasegura.room.dao.FireDao
import com.example.queimasegura.room.entities.Fire

class FireRepository(private val fireDao: FireDao) {
    val readData: LiveData<List<Fire>> = fireDao.readFiresData()

    suspend fun addFire(fire: Fire) {
        fireDao.addFire(fire)
    }

    suspend fun clearFires() {
        fireDao.clearFires()
    }

    suspend fun nextFireEn(): Fire? {
        return fireDao.nextFireEn()
    }

    suspend fun nextFirePt(): Fire? {
        return fireDao.nextFirePt()
    }
}