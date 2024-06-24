package com.example.queimasegura.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.queimasegura.room.entities.Fire


@Dao
interface FireDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFire(fire: Fire)

    @Query("DELETE FROM fire_table")
    suspend fun clearFires()

    @Query("SELECT * FROM fire_table ORDER BY date ASC")
    fun readFiresData(): LiveData<List<Fire>>

    @Query("SELECT * FROM fire_table WHERE status = 'Scheduled' OR status = 'Ongoing' ORDER BY date ASC LIMIT 1")
    suspend fun nextFireEn(): Fire?

    @Query("SELECT * FROM fire_table WHERE status = 'Agendado' OR status = 'Em Andamento' ORDER BY date ASC LIMIT 1")
    suspend fun nextFirePt(): Fire?
}