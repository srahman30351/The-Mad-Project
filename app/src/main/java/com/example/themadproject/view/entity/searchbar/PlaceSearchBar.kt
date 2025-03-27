import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.view.entity.searchbar.PredictionText
import com.example.themadproject.view.entity.searchbar.handleFetchPlace
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceSearchBar(
    viewModel: StaySafeViewModel,
    onPlaceSelected: (LatLng, String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    Log.d("SearchBar", "SearchBar composable is running")

    val handleSelect: (LatLng, String) -> Unit = { latLng, text ->
        onPlaceSelected(latLng, text)
        query = text
        expanded = false

    }
    DockedSearchBar(
        inputField = {
            InputField(
                query = query,
                onQueryChange = {
                    query = it
                    if (it.isEmpty()) predictions = emptyList()
                    expanded = true
                },
                onSearch = { if(predictions.isNotEmpty()) handleFetchPlace(predictions.first(), context, handleSelect, viewModel) },
                expanded = false,
                onExpandedChange = { },
                placeholder = { Text("Search for places") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                if (!expanded) expanded = true
                                else if (predictions.isNotEmpty()) handleFetchPlace(predictions.first(), context, handleSelect, viewModel)
                            }
                        )
                    )
                },
                trailingIcon = {
                    if (expanded || query.isNotEmpty()) Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                query = ""
                                expanded = false
                            }
                        )
                    )
                }
            )
        },
        expanded = expanded,

        onExpandedChange = { },
        content = {
            if (predictions.isNotEmpty()) {
                predictions.forEachIndexed { index, prediction ->
                    PredictionText( prediction, onClick = { handleFetchPlace(prediction, context, handleSelect, viewModel) })
                    if (index != predictions.lastIndex) {
                        HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    }
                }
            } else {
                Text(
                    text = "No places were found",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
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
                    Log.d(
                        "SearchBar",
                        "Predictions found: ${response.autocompletePredictions.size}"
                    )
                    predictions = response.autocompletePredictions
                }
                .addOnFailureListener { e ->
                    Log.e(
                        "SearchBar",
                        "Failed to fetch place details: ${e.localizedMessage}"
                    )
                }
        } else {
            predictions = emptyList()
        }
    }
}
