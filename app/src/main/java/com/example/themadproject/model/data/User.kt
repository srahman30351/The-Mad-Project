package com.example.myapplication.model.data

data class User(
    val UserUsername: String,
    val UserFirstname: String,
    val UserLastname: String,
    val UserPassword: String,
    val UserPhone: String,
    val UserImageURL: String,
    val UserLatitude: Double = -34.0,
    val UserLongitude: Double = 151.0,
    val UserTimestamp: Double = 0.0,
    val UserID: Int = 1 //The StaySafe API autoincrement the id
)