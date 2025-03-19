package com.example.myapplication.view

import android.content.ComponentCallbacks
import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.view.navigation.MapBackground
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.util.Locale
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
/*fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as AppCompatActivity
    val searchQuery = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyBpRd8pMrcC34T4riIish0azmEmyu8QreQ")
        }
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("user_screen") }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Navigate to Friends")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapBackground(
                modifier = Modifier
                    .matchParentSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = searchQuery.value,
                        onValueChange = { searchQuery.value = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )
                    IconButton(onClick = {
                        if (searchQuery.value.isNotEmpty()) {
                            coroutineScope.launch {
                                searchLocation(activity, searchQuery.value) { latLng ->
                                    if (latLng != null) {
                                        moveMap(activity, latLng, searchQuery.value)
                                    } else {
                                        Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun searchLocation(context: Context, location: String, callback: (LatLng?) -> Unit) {
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        geocoder.getFromLocationName(location, 1) { addressList ->
            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                callback(LatLng(address.latitude, address.longitude))
            } else {
                callback(null)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        callback(null)
    }
}
fun moveMap(activity: AppCompatActivity, latLng: LatLng, title: String) {
    val fragmentManager = activity.supportFragmentManager
    val mapFragment = fragmentManager.findFragmentByTag("map_fragment") as? SupportMapFragment
    mapFragment?.getMapAsync { googleMap ->
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng).title(title))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }
}

*/
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as AppCompatActivity

    // Initialize Places API
    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            Places.initialize(context, "YOUR_API_KEY")
        }
    }

    val placesClient = remember { Places.createClient(context) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("user_screen") }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Navigate to Friends")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapBackground(modifier = Modifier.matchParentSize())

            // Search Bar with Autocomplete
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AutocompleteSearchBar(activity, placesClient) { latLng, placeName ->
                    moveMap(activity, latLng, placeName)
                }
            }
        }
    }
}
@Composable
fun AutocompleteSearchBar(
    activity: AppCompatActivity,
    placesClient: PlacesClient,
    onPlaceSelected: (LatLng, String) -> Unit
) {
    val context = LocalContext.current
    val searchQuery = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            textStyle = TextStyle(fontSize = 16.sp)
        )
        IconButton(onClick = {
            if (searchQuery.value.isNotEmpty()) {
                coroutineScope.launch {
                    searchPlaces(activity, placesClient, searchQuery.value) { latLng, placeName ->
                        onPlaceSelected(latLng, placeName)
                    }
                }
            }
        }) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
    }
}
suspend fun searchPlaces(context: Context, placesClient: PlacesClient, query: String, callback: (LatLng, String) -> Unit) {
    withContext(Dispatchers.IO) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        val result = suspendCoroutine { continuation ->
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    if (response.autocompletePredictions.isNotEmpty()) {
                        val placeId = response.autocompletePredictions[0].placeId
                        getPlaceLatLng(placesClient, placeId, callback)
                    }
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    continuation.resume(Unit)
                }
        }
    }
}
suspend fun getPlaceLatLng(placesClient: PlacesClient, placeId: String, callback: (LatLng, String) -> Unit) {
    val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
    val request = FetchPlaceRequest.builder(placeId, placeFields).build()

    suspendCoroutine { continuation ->
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                response.place.latLng?.let {
                    callback(it, response.place.name ?: "Unknown Place")
                }
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                it.printStackTrace()
                continuation.resume(Unit)
            }
    }
}
fun moveMap(activity: AppCompatActivity, latLng: LatLng, title: String) {
    val fragmentManager = activity.supportFragmentManager
    val mapFragment = fragmentManager.findFragmentByTag("map_fragment") as? SupportMapFragment

    mapFragment?.getMapAsync { googleMap ->
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng).title(title))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }
}