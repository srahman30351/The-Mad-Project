package com.example.myapplication.model.data

data class Status(
    val StatusName: String,
    val StatusOrder: Int,
    val StatusID: Int = 1 //The StaySafe API autoincrement the id
)