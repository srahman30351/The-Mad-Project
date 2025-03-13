package com.example.myapplication.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    var userID: String,
    var userFirstname: String,
    var userLastname: String,
    var userPhone: String,
    var userUsername: String
)
