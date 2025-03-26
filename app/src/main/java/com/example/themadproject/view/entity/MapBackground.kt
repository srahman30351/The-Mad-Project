package com.example.myapplication.view.navigation

import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.myapplication.model.data.User
import coil.size.Size
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions


@Composable
fun MapBackground(modifier: Modifier = Modifier, user: User, selectedLocation: LatLng?, startPoint: LatLng?, endPoint: LatLng?, route1Polyline: PolylineOptions?, route2Polyline: PolylineOptions?, estTime: String, estTime2: String, friendsList: List<User>) {
    val context = LocalContext.current
    val mapView = remember { mutableStateOf<GoogleMap?>(null) }
    val userLocation = LatLng(user.UserLatitude, user.UserLongitude)
    val currentEstTime by rememberUpdatedState(estTime)
    val currentEstTime2 by rememberUpdatedState(estTime2)
    println(friendsList.size)

    AndroidView(
        factory = { context ->
            val mapFragment = SupportMapFragment.newInstance()
            val frameLayout = FrameLayout(context).apply {
                id = View.generateViewId()
            }

            (context as AppCompatActivity).supportFragmentManager
                .beginTransaction()
                .replace(frameLayout.id, mapFragment)
                .commit()

            mapFragment.getMapAsync { googleMap ->
                mapView.value = googleMap
                googleMap.uiSettings.isZoomControlsEnabled = true
                googleMap.uiSettings.isZoomGesturesEnabled = true
                googleMap.uiSettings.isScrollGesturesEnabled = true
                googleMap.addMarker(MarkerOptions().position(userLocation).title("Current Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                route1Polyline?.let {
                    googleMap.addPolyline(it)
                }
                route2Polyline?.let {
                    googleMap.addPolyline(it)
                }
                startPoint?.let {
                    googleMap.addMarker(MarkerOptions().position(it).title("Start Point"))
                }
                endPoint?.let {
                    googleMap.addMarker(MarkerOptions().position(it).title("End Point"))
                }
                Log.d("MapBackground", "friendsList size: ${friendsList.size}")
                friendsList.forEach { friend ->
                    val friendsLocation = LatLng(friend.UserLatitude, friend.UserLongitude)
                    Log.d("MapBackground", "users cords $friendsLocation")
                    googleMap.addMarker(
                        MarkerOptions().position(friendsLocation).title(friend.UserUsername)
                    )
                }
                if (startPoint != null && endPoint != null) {
                    val bounds = LatLngBounds.Builder()
                        .include(startPoint)
                        .include(endPoint)
                        .build()
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                }

            }
            frameLayout
        },
        modifier = modifier.fillMaxSize()
    )
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Estimated Time to Start: $currentEstTime minutes",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Estimated Time to end: $currentEstTime2 minutes",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

    }
    LaunchedEffect(startPoint, endPoint, route1Polyline, route2Polyline) {
        mapView.value?.let { googleMap ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(userLocation).title("Current Location"))
            startPoint?.let {
                googleMap.addMarker(MarkerOptions().position(it).title("Start Point"))
            }
            endPoint?.let {
                googleMap.addMarker(MarkerOptions().position(it).title("End Point"))
            }
            route1Polyline?.let {
                googleMap.addPolyline(it)
            }
            route2Polyline?.let {
                googleMap.addPolyline(it)
            }
            if (startPoint != null && endPoint != null) {
                val bounds = LatLngBounds.Builder()
                    .include(startPoint)
                    .include(endPoint)
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }
    }
}

