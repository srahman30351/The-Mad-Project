package com.example.myapplication.model.data

data class Position(
    val PositionActivityID: Int,
    val PositionActivityName: String?,
    val PositionID: Int,
    val PositionLongitude: Double = 0.0,
    val PositionLatitude: Double = 0.0,
)