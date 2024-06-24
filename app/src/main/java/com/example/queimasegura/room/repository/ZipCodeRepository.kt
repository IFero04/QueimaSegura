package com.example.queimasegura.room.repository

import androidx.lifecycle.LiveData
import com.example.queimasegura.room.dao.ZipCodeDao
import com.example.queimasegura.room.entities.ZipCode

class ZipCodeRepository(private val zipCodeDao: ZipCodeDao) {
    val readData: LiveData<List<ZipCode>> = zipCodeDao.readZipsData()

    suspend fun addZipcode(zipCode: ZipCode) {
        if(!zipCodeDao.isZipCodeExists(zipCode.id)){
            zipCodeDao.addZip(zipCode)
        }
    }

    suspend fun clearZips() {
        zipCodeDao.clearZips()
    }
}