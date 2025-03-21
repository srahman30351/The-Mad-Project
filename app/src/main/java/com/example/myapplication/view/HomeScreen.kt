package com.example.myapplication.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.view.navigation.MapBackground
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.model.AutocompletePrediction
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

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
                    .matchParentSize(),
                selectedLocation = selectedLocation
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                SearchBar { place ->
                    selectedLocation = place
                    Log.d("HomeScreen", "Selected location: $selectedLocation")
                }

            }
        }
    }
}
@Composable
fun SearchBar(onPlaceSelected: (LatLng) -> Unit) {
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    Log.d("SearchBar", "SearchBar composable is running")

    TextField(
        value = query,
        onValueChange = { query2 ->
            query = query2
            predictions = if (query2.isEmpty()) {
                emptyList()
            } else  {
                predictions
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        placeholder = { Text("Search for a place")}
        )
    LaunchedEffect(query) {
        if (query.length > 2) {
            Log.d("SearchBar", "Searching for: $query")
            val placesClient = Places.createClient(context)
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    Log.d("SearchBar", "Predictions found: ${response.autocompletePredictions.size}")
                        predictions = response.autocompletePredictions
                }
                .addOnFailureListener() { e ->
                    Log.e("SearchBar", "Failed to fetch place details: ${e.localizedMessage}")
                }
        } else {
            predictions = emptyList()
        }
    }
    if (predictions.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
    }
    LazyColumn(modifier = Modifier.fillMaxWidth()){
        items(predictions) { prediction ->
            Text(
                text = prediction.getFullText(null).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val placeId = prediction.placeId
                        val placesClient = Places.createClient(context)
                        val placeRequest = FetchPlaceRequest.builder(placeId, listOf(Place.Field.LAT_LNG)).build()

                        placesClient.fetchPlace(placeRequest)
                            .addOnSuccessListener { placeResponse ->
                                placeResponse.place.latLng?.let { latLng ->
                                    onPlaceSelected(latLng)
                                }
                            }
                    }
                    .padding(12.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
        }
    }


}

