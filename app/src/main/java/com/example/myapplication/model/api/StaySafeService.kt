package com.example.myapplication.model.api

import com.example.myapplication.model.data.Location
import retrofit2.Response
import retrofit2.http.GET

interface StaySafeService {

    @GET("locations")
    suspend fun getLocations(): List<Location>

}