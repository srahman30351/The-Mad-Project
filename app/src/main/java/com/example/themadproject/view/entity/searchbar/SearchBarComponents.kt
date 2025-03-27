package com.example.themadproject.view.entity.searchbar

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.data.Location
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.kotlin.place
import com.google.android.libraries.places.api.net.FetchPlaceRequest

@Composable
fun PredictionText(
    prediction: AutocompletePrediction,
    onClick: () -> Unit,
) {
    var predictedText = prediction.getFullText(null).toString()
    Text(
        text = predictedText,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable(onClick =  onClick)
    )
}

fun handleFetchPlace(
    prediction: AutocompletePrediction,
    context: Context,
    onFetch: (LatLng, String) -> Unit,
    viewModel: StaySafeViewModel
) {
    var predictedText = prediction.getFullText(null).toString()
    val placeId = prediction.placeId
    val placesClient = Places.createClient(context)
    val placeFields = listOf(Place.Field.LAT_LNG)
    val placeRequest =
        FetchPlaceRequest.builder(placeId, placeFields)
            .build()

    placesClient.fetchPlace(placeRequest)
        .addOnSuccessListener { placeResponse ->

            val place = placeResponse.place
            val location = place.location
            if (location == null) {
                viewModel.showSnackbar("Unable to find location or address", "Error")
            } else {
                onFetch(location, predictedText)
            }
        }
}

fun handleFetchLocation(
    prediction: AutocompletePrediction,
    context: Context,
    onFetch: (Location) -> Unit,
    viewModel: StaySafeViewModel
) {
    var predictedText = prediction.getFullText(null).toString()
    val placeId = prediction.placeId
    val placesClient = Places.createClient(context)
    val placeFields = listOf(
        Place.Field.LAT_LNG,
        Place.Field.DISPLAY_NAME,
        Place.Field.FORMATTED_ADDRESS,
        Place.Field.ADDRESS_COMPONENTS
    )
    val placeRequest =
        FetchPlaceRequest.builder(placeId, placeFields)
            .build()

    placesClient.fetchPlace(placeRequest)
        .addOnSuccessListener { placeResponse ->

            val place = placeResponse.place
            val placeName = place.displayName
            val location = place.location
            val address = place.formattedAddress
            val postcode = place.addressComponents?.asList()
                ?.firstOrNull { component -> "postal_code" in component.types }?.name

            if (location == null || address == null || placeName == null) {
                viewModel.showSnackbar("Unable to find location or address", "Error")
            } else {
                onFetch(
                    Location(
                        LocationName = placeName,
                        LocationDescription = predictedText,
                        LocationAddress = address,
                        LocationLatitude = location.latitude,
                        LocationLongitude = location.longitude,
                        LocationPostcode = postcode ?: "Not Available"
                    )
                )
            }
        }
    }
