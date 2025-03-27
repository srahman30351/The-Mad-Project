package com.example.myapplication.view.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.compose.foundation.content.MediaType.Companion.Image
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.Coil
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.myapplication.model.data.User
import coil.size.Size
import com.example.myapplication.model.data.Activity
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.model.data.LatLngCords
import com.example.themadproject.view.entity.marker.PinIconMarker
import com.example.themadproject.view.entity.sheet.ProfileBottomSheet
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.launch


@SuppressLint("PotentialBehaviorOverride")
@Composable
fun MapBackground(
    modifier: Modifier = Modifier,
    user: User,
    selectedLocation: LatLng?,
    startPoint: LatLng?, endPoint: LatLng?,
    route1Polyline: PolylineOptions?,
    route2Polyline: PolylineOptions?,
    estTime: String,
    estTime2: String,
    friendsList: List<User>,
    viewModel: StaySafeViewModel,
    selectedFriend: User?,
    profileState: Boolean,
    onProfileSheetChange: (Boolean, User) -> Unit,
    clearMarkers: Boolean,
    userCords: LatLng?,
    isActivityStarted: Boolean) {
    val context = LocalContext.current
    val mapView = remember { mutableStateOf<GoogleMap?>(null) }
    val userLocation = userCords ?: LatLng(user.UserLatitude, user.UserLongitude)
    val currentEstTime by rememberUpdatedState(estTime)
    val currentEstTime2 by rememberUpdatedState(estTime2)
    val coroutineScope = rememberCoroutineScope()
    val activityMarkers = remember { mutableStateListOf<Marker>() }
    val userMarker = remember { mutableStateOf<Marker?>(null) }
    val clearTheMap = clearMarkers
    Log.d("MapBackground", "userCords are: $userCords")
    LaunchedEffect(userCords) {
        mapView.value?.apply {
            userMarker.value?.remove()
            val icons = bitmapFormat(context, user.UserImageURL)
            icons?.let {
                val newMarker =
                    addMarker(MarkerOptions().position(userLocation).title(user.UserUsername).icon(BitmapDescriptorFactory.fromBitmap(it)))
                userMarker.value = newMarker
                animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            }
        }
    }
    LaunchedEffect(selectedLocation) {
        mapView.value?.apply {
            selectedLocation?.let { location ->
                animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                addMarker(MarkerOptions().position(location).title("Selected Location"))
            }
        }
    }


    LaunchedEffect(clearTheMap) {
        mapView.value?.apply {
            if (clearMarkers) {
                mapView.value?.let { googleMap ->
                    googleMap.clear()
                    val icons1 = bitmapFormat(context, user.UserImageURL)
                    icons1?.let {
                        val newMarker =
                            addMarker(
                                MarkerOptions().position(userLocation).title(user.UserUsername)
                                    .icon(BitmapDescriptorFactory.fromBitmap(it))
                            )
                        userMarker.value = newMarker
                        animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                    }
                    friendsList.forEach { friend ->
                        val friendsLocation = LatLng(friend.UserLatitude, friend.UserLongitude)
                        coroutineScope.launch {
                            val icons = bitmapFormat(context, friend.UserImageURL)
                            icons?.let {
                                addMarker(
                                    MarkerOptions().position(friendsLocation)
                                        .title(friend.UserUsername)
                                        .icon(BitmapDescriptorFactory.fromBitmap(it))
                                )
                            }
                        }
                    }
                }
            }
        }
    }

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
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                route1Polyline?.let {
                    googleMap.addPolyline(it)
                }
                route2Polyline?.let {
                    googleMap.addPolyline(it)
                }
                friendsList.forEach { friend ->
                    val friendsLocation = LatLng(friend.UserLatitude, friend.UserLongitude)
                    coroutineScope.launch {
                        val icons = bitmapFormat(context, friend.UserImageURL)
                        icons?.let {
                            googleMap.addMarker(
                                MarkerOptions().position(friendsLocation).title(friend.UserUsername).icon(BitmapDescriptorFactory.fromBitmap(it))
                            )
                        }
                    }
                }
                if (startPoint != null && endPoint != null) {
                    val bounds = LatLngBounds.Builder()
                        .build()
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))

            }
            frameLayout
        },
        modifier = modifier.fillMaxSize()
    )
    if (isActivityStarted) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Estimated Time of arrival: $currentEstTime minutes",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
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

suspend fun bitmapFormat(context: Context, url: String): Bitmap? {
    val imageLoader = ImageLoader(context)

        val request = ImageRequest.Builder(context)
            .data(url)
            .size(100, 100)
            .target{ drawable ->
                if (drawable is BitmapDrawable) {
                    val bitmap = drawable.bitmap
                }
            }
            .build()
        val result = imageLoader.execute(request)
       return if (result is SuccessResult) {
           val drawable = result.drawable
           if (drawable is BitmapDrawable){
               drawable.bitmap
           } else {
               null
           }
       } else  {
           null
       }
}

