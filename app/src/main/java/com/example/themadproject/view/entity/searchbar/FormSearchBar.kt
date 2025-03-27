import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.data.Location
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.view.entity.searchbar.PredictionText
import com.example.themadproject.view.entity.searchbar.handleFetchLocation
import com.example.themadproject.view.entity.searchbar.handleFetchPlace
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSearchBar(
    onPlaceSelected: (Location) -> Unit,
    onDismiss: () -> Unit,
    label: String,
    viewModel: StaySafeViewModel
) {
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    Log.d("SearchBar", "SearchBar composable is running")

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val handleSelect: (Location) -> Unit = { location ->
        onPlaceSelected(location)
        query = location.LocationDescription
    }

    SearchBar(
        inputField = {
            InputField(
                modifier = Modifier.focusRequester(focusRequester),
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    if (predictions.isNotEmpty()) handleFetchLocation(
                        predictions.first(),
                        context,
                        handleSelect,
                        viewModel
                    )
                },
                expanded = false,
                onExpandedChange = { },
                placeholder = { Text("Search place for $label") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                if (predictions.isNotEmpty()) {
                                    handleFetchLocation(
                                        predictions.first(),
                                        context,
                                        handleSelect,
                                        viewModel
                                    )
                                }
                            }
                        )
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                query = ""
                                onDismiss()
                            }
                        )
                    )
                }
            )
        },
        expanded = true,

        onExpandedChange = { },
        content = {
            if (predictions.isNotEmpty()) {
                predictions.forEachIndexed { index, prediction ->
                    PredictionText(prediction, onClick = { handleFetchLocation(prediction, context, handleSelect, viewModel) })
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