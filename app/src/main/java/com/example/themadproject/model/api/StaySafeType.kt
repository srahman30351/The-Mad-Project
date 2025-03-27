package com.example.themadproject.model.api

sealed class StaySafe(val type: String) {
    object User: StaySafe("users")
    object Activity : StaySafe("activities")
    object Position : StaySafe("positions")
    object Location : StaySafe("locations")
    object Status : StaySafe("status")
}