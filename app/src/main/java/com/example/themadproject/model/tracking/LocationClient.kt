package com.example.themadproject.model.tracking

import com.example.myapplication.model.data.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<android.location.Location>

    class LocationException(message: String): Exception()
}