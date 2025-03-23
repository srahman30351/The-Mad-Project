package com.example.myapplication.model.api

import com.example.myapplication.model.data.Activity
import com.example.myapplication.model.data.Location
import com.example.myapplication.model.data.Position
import com.example.myapplication.model.data.Status
import com.example.myapplication.model.data.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("users")
    suspend fun getUsersByUsername(
    @Query("UserUsername") username: String
    ): Response<List<User>>

    @POST("users")
    suspend fun postUser(@Body user: User): Response<ResponseBody>

    @PUT("users/{id}")
    suspend fun editUser(
        @Path("id") id: Int,
        @Body user: User
    ): Response<ResponseBody>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<ResponseBody>
}