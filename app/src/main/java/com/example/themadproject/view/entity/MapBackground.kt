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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.myapplication.model.data.User
import coil.size.Size


@Composable
fun MapBackground(modifier: Modifier = Modifier, user: User, selectedLocation: LatLng?) {
    val context = LocalContext.current
    val mapView = remember { mutableStateOf<GoogleMap?>(null) }
    val userLocation = LatLng(user.UserLatitude, user.UserLongitude)

    Log.d("MapBackground", "current location: $userLocation")
    Log.d("MapBackground", "MapBackground composable is running")
    Log.d("MapBackground", "Received selectedLocation: $selectedLocation")
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
                googleMap.addMarker(MarkerOptions().position(userLocation).title("Current Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))

                /*selectedLocation?.let { newLocation ->
                    Log.d("MapBackground", "Updating map with selected location: $selectedLocation")
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(newLocation).title("selectedLocation"))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 12f))
                }*/
            }
            frameLayout
        },
        modifier = modifier.fillMaxSize()
    )
    LaunchedEffect(selectedLocation) {
        mapView.value?.let { googleMap ->
            selectedLocation?.let { newLocation ->
                Log.d("MapBackground", "updating map with selected location from LaunchedEffect: $newLocation")
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(newLocation).title("Selected location"))
                googleMap.animateCamera((CameraUpdateFactory.newLatLngZoom(newLocation, 15f)))
            }
        }
    }
}

