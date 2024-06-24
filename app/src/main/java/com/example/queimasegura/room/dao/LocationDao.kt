package com.example.queimasegura.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.queimasegura.room.entities.Location

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocation(location: Location)

    @Query("DELETE FROM location_table")
    suspend fun clearLocations()

    @Query("SELECT * FROM location_table LIMIT 25")
    fun readLocationData(): LiveData<List<Location>>
}