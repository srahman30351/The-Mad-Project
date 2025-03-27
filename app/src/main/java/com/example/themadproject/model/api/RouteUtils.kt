package com.example.themadproject.model.api

import android.util.Log
import androidx.navigation.ActivityNavigator
import com.example.myapplication.model.api.StaySafeClient
import com.example.themadproject.model.data.LatLngCords
import com.example.themadproject.model.data.LatLngWrapper
import com.example.themadproject.model.data.LocationWrapper
import com.example.themadproject.model.data.RouteRequest
import com.example.themadproject.model.data.RouteResponse
import com.google.android.gms.common.api.internal.ApiKey
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.EncodedPolyline
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RouteUtils {
    private const val BASE_URL = "https://routes.googleapis.com/"
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor{ chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Goog-FieldMask", "routes.distanceMeters,routes.duration,routes.polyline.encodedPolyline")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(RouteUtils.logging)
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val routesAPIService = retrofit.create(RoutesAPIService::class.java)

    suspend fun getRoute(origin: LatLng, destination: LatLng, apiKey: String): RouteResponse {
        val requestBody = RouteRequest(
            origin = LocationWrapper(LatLngWrapper(LatLngCords(origin.latitude, origin.longitude))),
            destination = LocationWrapper(LatLngWrapper(LatLngCords(destination.latitude, destination.longitude))),
            travelMode = "WALK",
            computeAlternativeRoutes = false

        )
        return try {
            routesAPIService.getRoute(requestBody, apiKey)
        } catch (e: Exception) {
            throw   Exception("Error fetching route: ${e.message}")
        }

    }

    fun decodePolyline(encodedPolyline: String): List<LatLng> {
        return com.google.maps.android.PolyUtil.decode(encodedPolyline)
    }
}
