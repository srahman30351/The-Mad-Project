package com.example.myapplication.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    var UserID: Int,
    var UserFirstname: String,
    var UserLastname: String,
    var UserPhone: String,
    var UserUsername: String
)
