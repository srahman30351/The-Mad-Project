package com.example.myapplication.model

import com.example.myapplication.model.data.User
import retrofit2.http.GET

interface StaySafeService {
    @GET("users")
    fun getUsers(): List<User>
}