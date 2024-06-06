package com.example.queimasegura.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val id: String,
    var sessionId: String?,
    val email: String,
    val fullName: String,
    val nif: Int,
    val type: Int,
)
