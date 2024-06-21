package com.example.queimasegura.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.queimasegura.room.entities.Status


@Dao
interface StatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStatus(status: Status)

    @Query("DELETE FROM status_table")
    suspend fun clearStatus()

    @Query("SELECT * FROM status_table LIMIT 1")
    fun readStatusData(): LiveData<Status>
}