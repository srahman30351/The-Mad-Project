package com.example.themadproject.view

import FormSearchBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.model.data.Activity
import com.example.myapplication.model.data.Location
import com.example.myapplication.model.data.Status
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(navController: NavController, viewModel: StaySafeViewModel) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState.value) },
        topBar = {
            TopAppBar(
                title = { Text("Create Activity") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back arrow",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        //Mutable states --------------------------------------------------------------------------------------
        var name by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var arriveSearchState by remember { mutableStateOf(false) }
        var fromSearchState by remember { mutableStateOf(false) }

        var arriveLocation by remember { mutableStateOf<Location?>(null) }
        var fromLocation by remember { mutableStateOf<Location?>(null) }


        //Handlers --------------------------------------------------------------------------------------------

        var handleFrom: (Location) -> Unit = { location ->
            fromLocation = location
            fromSearchState = false
        }
        var handleArrive: (Location) -> Unit = { location ->
            arriveLocation = location
            arriveSearchState = false
        }

        if (fromSearchState) FormSearchBar(
            onPlaceSelected = handleFrom,
            onDismiss = { fromSearchState = false },
            label = "starting point",
            viewModel
        )
        if (arriveSearchState) FormSearchBar(
            onPlaceSelected = handleArrive,
            onDismiss = { arriveSearchState = false },
            label = "end destination",
            viewModel
        )

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 64.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Activity Name") },
                singleLine = true
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Activity Description") },
                maxLines = 5
            )
            Column {
                SearchTextField(
                    query = fromLocation?.LocationDescription ?: "",
                    label = "Starting Place",
                    onShow = { fromSearchState = true }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = {})
                    Text("Start at existing location")
                }
                SearchTextField(
                    query = arriveLocation?.LocationDescription ?: "",
                    label = "Destination",
                    onShow = { arriveSearchState = true }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = {})
                    Text("Have no end destination")
                }
            }
            Button(
                modifier = Modifier.width(200.dp),
                onClick = {
                    verifyActivity(
                        name, description, fromLocation, arriveLocation, viewModel, navController
                    )
                }
            ) {
                Text(text = "Create Activity")
            }
        }
    }
}

private fun verifyActivity(
    name: String,
    description: String,
    fromLocation: Location?,
    arriveLocation: Location?,
    viewModel: StaySafeViewModel,
    navController: NavController
) {
    println(fromLocation)
    println(arriveLocation)
    if (name.isBlank() || description.isBlank() || fromLocation == null || arriveLocation == null) {
        viewModel.showSnackbar("Please fill in the fields", "Error")
        return
    } else {
        viewModel.user.value?.let { user ->
            viewModel.postLocation(fromLocation) { fromID ->
                viewModel.postLocation(arriveLocation) { arriveID ->
                    viewModel.postData(
                        Activity(
                            ActivityName = name,
                            ActivityDescription = description,
                            ActivityLeave = "2025-03-27T00:51:07.000Z",
                            ActivityArrive = "2025-03-27T00:51:07.000Z",
                            ActivityFromID = fromID,
                            ActivityToID = arriveID,
                            ActivityUserID = user.UserID,
                            ActivityStatusID = 1
                        )
                    )
                }
            }


        }
    }
}


@Composable
fun SearchTextField(
    query: String,
    label: String,
    onShow: () -> Unit,
) {
    Box {
        OutlinedTextField(
            value = query,
            onValueChange = { },
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Place"
                )
            },
            singleLine = true
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onShow
                )
        )
    }
}