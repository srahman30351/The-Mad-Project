package com.example.themadproject.model.tracking

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.myapplication.model.data.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private val _locationFlow = MutableSharedFlow<Location>(replay = 1)
    val locationFlow = _locationFlow.asSharedFlow()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    data class Location(
        val latitude: Double,
        val longitude: Double
    )
    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
        applicationContext,
        LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        locationClient.getLocationUpdates(10000L)
            .onEach { location ->
                _locationFlow.emit(location.toCustomLocation())
            }
            .launchIn(serviceScope)
    }

    private fun stop() {
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
    fun android.location.Location.toCustomLocation(): Location {
        return Location(latitude = this.latitude, longitude = this.latitude)
    }
}