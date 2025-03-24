package com.example.themadproject.model.api

import androidx.navigation.ActivityNavigator
import com.example.themadproject.model.data.Location
import com.example.themadproject.model.data.RouteRequest
import com.example.themadproject.model.data.RouteResponse
import com.google.android.gms.common.api.internal.ApiKey
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.EncodedPolyline
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RouteUtils {
    private const val BASE_URL = "https://routes.googleapis.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val routesAPIService = retrofit.create(RoutesAPIService::class.java)

    suspend fun getRoute(origin: LatLng, destination: LatLng, apiKey: String): RouteResponse {
        val requestBody = RouteRequest(
            origin = Location(LatLng(origin.latitude, origin.longitude)),
            destination = Location(LatLng(destination.latitude, destination.longitude))

        )
        return routesAPIService.getRoute(requestBody, apiKey)
    }
    fun decodePolyline(encodedPolyline: String): List<LatLng> {
        return com.google.maps.android.PolyUtil.decode(encodedPolyline)
    }
}
