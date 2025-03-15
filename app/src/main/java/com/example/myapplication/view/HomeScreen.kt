package com.example.myapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.myapplication.view.navigation.MapBackground
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: StaySafeViewModel = viewModel()){

    val data = viewModel.staySafeData.collectAsState().value
    if (data.isNotEmpty()) {
        println(data)
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
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .zIndex(1f),
//                verticalArrangement = Arrangement.Bottom,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//            }
        }
    }
}

