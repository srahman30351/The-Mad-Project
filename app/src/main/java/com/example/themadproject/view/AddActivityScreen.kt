package com.example.themadproject.view

import FormSearchBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
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
        var toDestination = remember { mutableStateOf("") }
        var fromDestination = remember { mutableStateOf("") }
        var searchState1 by remember { mutableStateOf(false) }
        var searchState2 by remember { mutableStateOf(false) }

        //Handlers --------------------------------------------------------------------------------------------

        var handleToDestination: (LatLng, String) -> Unit = { location, placeName ->

        }

        var handleFromDestination: (LatLng, String) -> Unit = { location, placeName ->

        }
        if (searchState1) FormSearchBar(
            query = toDestination,
            onPlaceSelected = handleToDestination,
            onDismiss = { searchState1 = false },
            label = "end destination"
        )
        if (searchState2) FormSearchBar(
            query = fromDestination,
            onPlaceSelected = handleFromDestination,
            onDismiss = { searchState2 = false },
            label = "starting destination"
        )
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 64.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
            SearchTextField(
                query = toDestination,
                label = "End Destination",
                onShow = { searchState1 = true })

            SearchTextField(
                query = fromDestination,
                label = "Starting Destination",
                onShow = { searchState2 = true }
            )
            Button(
                modifier = Modifier.width(200.dp),
                onClick = {  }
            ) {
                Text(text = "Add")
            }
        }
    }
}

@Composable
fun SearchTextField(query: MutableState<String>, label: String, onShow: () -> Unit) {
    Box {
        OutlinedTextField(
            value = query.value,
            onValueChange = { query.value = it },
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