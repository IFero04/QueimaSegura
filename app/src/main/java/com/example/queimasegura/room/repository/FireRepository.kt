package com.example.queimasegura.room.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.queimasegura.R
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

    suspend fun getNextFire(): Fire? {
        val statuses = listOf((R.string.fire_status_scheduled.toString()), R.string.fire_status_ongoing.toString())
        Log.d("LIST STATUS", statuses.toString())
        return fireDao.nextFire(statuses)
    }
}