package com.example.myapplication.model.data

data class Activity(
    val ActivityName: String,
    val ActivityDescription: String,
    val ActivityFromName: String? = null,
    val ActivityLeave: String,
    val ActivityToName: String? = null,
    val ActivityArrive: String,
    val ActivityFromID: Int,
    val ActivityStatusID: Int,
    val ActivityStatusName: String? = null,
    val ActivityToID: Int,
    val ActivityUserID: Int,
    val ActivityUsername: String? = null,
    val ActivityID: Int = 1 //The StaySafe API autoincrement the id
)