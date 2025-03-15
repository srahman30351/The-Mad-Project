package com.example.myapplication.model.api

import com.example.myapplication.model.data.Activity
import com.example.myapplication.model.data.Location
import com.example.myapplication.model.data.Position
import com.example.myapplication.model.data.Status
import com.example.myapplication.model.data.User
import retrofit2.http.GET

interface StaySafeService {

    @GET("activities")
    suspend fun getActivities(): List<Activity>

    @GET("locations")
    suspend fun getLocations(): List<Location>

    @GET("positions")
    suspend fun getPositions(): List<Position>

    @GET("status")
    suspend fun getStatus(): List<Status>

    @GET("users")
    suspend fun getUsers(): List<User>
}