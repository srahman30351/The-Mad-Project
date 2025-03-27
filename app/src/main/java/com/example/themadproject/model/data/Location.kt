package com.example.myapplication.model.data

data class Location(
    val LocationAddress: String,
    val LocationDescription: String,
    val LocationLatitude: Double,
    val LocationLongitude: Double,
    val LocationName: String,
    val LocationPostcode: String,
    val LocationID: Int = 1 //The StaySafe API autoincrement the id
)