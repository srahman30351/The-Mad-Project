package com.example.myapplication.model.data

data class Position(
    val PositionActivityID: Int,
    val PositionActivityName: String?,
    val PositionID: Int = 1 //The StaySafe API autoincrement the id
)