package com.example.queimasegura.room.repository

import androidx.lifecycle.LiveData
import com.example.queimasegura.room.dao.UserDao
import com.example.queimasegura.room.entities.User
import android.util.Log

class UserRepository(private val userDao: UserDao) {
    val readUserData: LiveData<User> = userDao.readUserData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    suspend fun deleteAll(){
        userDao.deleteAll()
    }
}