package com.example.myapplication.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.myapplication.view.navigation.MapBackground
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController, viewModel: StaySafeViewModel = viewModel()){

    val locations = viewModel.locations.collectAsState().value
    if (locations.isNotEmpty()) {
        println(locations[0].toString())
        MapBackground()
    }
    }

