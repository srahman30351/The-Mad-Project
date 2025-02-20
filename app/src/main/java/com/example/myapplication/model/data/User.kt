package com.example.myapplication.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    var UserID: String,
    var UserFirstname: String,
    var UserLastname: String,
    var UserPhone: String,
    var UserUsername: String
)
