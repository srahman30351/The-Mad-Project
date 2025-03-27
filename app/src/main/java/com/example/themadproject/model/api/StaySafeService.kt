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

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("users/contacts/{id}?=")
    suspend fun getUsers(@Path("id") userID: Int, @Query("UserContactLabel") label: String): Response<List<User>>

    @GET("users")
    suspend fun getUsersByUsername(@Query("UserUsername") username: String): Response<List<User>>

    @GET("activities")
    suspend fun getActivities(): Response<List<Activity>>

    @GET("activities/users/{id}")
    suspend fun getActivitiesByUserID(@Path("id") userID: Int): Response<List<Activity>>

    @GET("locations")
    suspend fun getLocations(): Response<List<Location>>

    @GET("positions")
    suspend fun getPositions(): Response<List<Position>>

    @GET("status")
    suspend fun getStatus(): Response<List<Status>>

    @POST("locations")
    suspend fun postLocation(@Body location: Location) : Response<List<Location>>

    //Generalised POST, PUT, and DELETE

    @POST("{to}")
    suspend fun postData(@Path("to") to: String, @Body data: Any) : Response<ResponseBody>

    @PUT("{to}/{id}")
    suspend fun putData(@Path("to") to: String, @Path("id") id: Int, @Body data: Any) : Response<ResponseBody>

    @DELETE("{to}/{id}")
    suspend fun deleteData(@Path("to") to: String, @Path("id") id: Int) : Response<ResponseBody>
}