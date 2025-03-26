package com.example.themadproject.view.entity.searchbar

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest

@Composable
fun PredictionText(
    prediction: AutocompletePrediction,
    context: Context,
    onSelect: (LatLng, String) -> Unit
) {
    var predictedText = prediction.getFullText(null).toString()
    Text(
        text = predictedText,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                fetchPlace(prediction, context, onSelect)
            }
    )
}

fun fetchPlace(
    prediction: AutocompletePrediction,
    context: Context,
    onFetch: (LatLng, String) -> Unit
) {
    var predictedText = prediction.getFullText(null).toString()
    val placeId = prediction.placeId
    val placesClient = Places.createClient(context)
    val placeRequest =
        FetchPlaceRequest.builder(placeId, listOf(Place.Field.LAT_LNG))
            .build()

    placesClient.fetchPlace(placeRequest)
        .addOnSuccessListener { placeResponse ->
            placeResponse.place.location?.let { latLng ->
                onFetch(latLng, predictedText)
            }
        }
}
