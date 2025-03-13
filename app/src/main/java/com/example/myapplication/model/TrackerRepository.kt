package com.example.myapplication.model

import androidx.annotation.WorkerThread
import com.example.myapplication.model.data.User
import kotlinx.coroutines.flow.Flow

class TrackerRepository(private val _trackerDao: TrackerDao) {

    val fetchAllUsers: Flow<List<User>> = _trackerDao.fetchAllUsers()
    @WorkerThread
    suspend fun insertUser(user: User) {
        _trackerDao.insertUser(user)
    }

    @WorkerThread
    suspend fun insertUsers(users: List<User>) {
        _trackerDao.insertUsers(users)
    }

    @WorkerThread
    suspend fun updateUser(user: User) {
        _trackerDao.updateUser(user)
    }

    @WorkerThread
    suspend fun deleteUser(user: User) {
        _trackerDao.deleteUser(user)
    }
}