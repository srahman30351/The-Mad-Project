package com.example.myapplication.view.navigation

import android.view.View
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptorFactory

@Composable
fun MapBackground(modifier: Modifier = Modifier) {
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
                val sydney = LatLng(-34.0, 151.0)
                googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
            }
/*
            val drawable = ContextCompat.getDrawable()

            val markerOption = MarkerOptions()
            markerOption.position(LatLng(-34.0, 151.0))
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(r.drawable.)))
*/
            frameLayout
        },
        modifier = modifier.fillMaxSize()
    )
}

