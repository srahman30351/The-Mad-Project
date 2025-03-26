import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.themadproject.view.entity.searchbar.PredictionText
import com.example.themadproject.view.entity.searchbar.fetchPlace
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSearchBar(
    query: MutableState<String>,
    onPlaceSelected: (LatLng, String) -> Unit,
    onDismiss: () -> Unit,
    label: String
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    Log.d("SearchBar", "SearchBar composable is running")

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val onSelect: (LatLng, String) -> Unit = { latLng, text ->
        onPlaceSelected(latLng, text)
        query.value = text

    }
    SearchBar(
        inputField = {
            InputField(
                modifier = Modifier.focusRequester(focusRequester),
                query = query.value,
                onQueryChange = { query.value = it },
                onSearch = {
                    if (predictions.isNotEmpty()) fetchPlace(
                        predictions.first(),
                        context,
                        onSelect
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
                                    fetchPlace(predictions.first(), context, onSelect)
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
                                query.value = ""
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
                    PredictionText(prediction, context, onSelect)
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
        if (query.value.length > 2) {
            Log.d("SearchBar", "Searching for: $query.value")
            val placesClient = Places.createClient(context)
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query.value)
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