package com.example.queimasegura.room.repository

import androidx.lifecycle.LiveData
import com.example.queimasegura.room.dao.LocationDao
import com.example.queimasegura.room.entities.Location

class LocationRepository(private val locationDao: LocationDao) {
    val readData: LiveData<List<Location>> = locationDao.readLocationData()

    suspend fun addLocation(location: Location) {
        locationDao.addLocation(location)
    }

    suspend fun clearLocations() {
        locationDao.clearLocations()
    }
}