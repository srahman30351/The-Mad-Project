package com.example.myapplication.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.myapplication.model.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackerDao {

    @Query("SELECT * FROM user_table")
    fun fetchAllUsers(): Flow<List<User>>

    @Upsert
    suspend fun insertUser(user: User)

    @Upsert
    suspend fun insertUsers(users: List<User>)

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User))

}