package com.example.themadproject.model.api

import com.example.themadproject.model.data.RouteRequest
import com.example.themadproject.model.data.RouteResponse
import com.google.android.gms.common.api.internal.ApiKey
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface RoutesAPIService {
    @POST("directions/v2:computeRoutes")
    suspend fun getRoute(
        @Body requestBody: RouteRequest,
        @Query("key") apiKey: String
    ): RouteResponse
}