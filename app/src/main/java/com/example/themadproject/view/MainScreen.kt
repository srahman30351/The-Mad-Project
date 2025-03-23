package com.example.themadproject.view

import PlaceSearchBar
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.myapplication.view.navigation.MapBackground
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.view.entity.sheet.SheetItem
import com.example.themadproject.R
import com.example.themadproject.view.entity.sheet.ActivityBottomSheet
import com.example.themadproject.view.entity.sheet.FriendBottomSheet
import com.example.themadproject.view.entity.sheet.ProfileBottomSheet
import com.example.themadproject.view.entity.sheet.SettingsBottomSheet
import com.google.android.gms.maps.model.LatLng

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: StaySafeViewModel
) {
    viewModel.loadContent() //Loads all content needed once screen is composed
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var activitySheetState by remember { mutableStateOf(false) }
    var friendSheetState by remember { mutableStateOf(false) }
    var profileSheetState by remember { mutableStateOf(false) }
    var settingsSheetState by remember { mutableStateOf(false) }
    val sheetItems = listOf(
        SheetItem(
            "Itineraries", R.drawable.travel, activitySheetState,
            onShow = {
                activitySheetState = true
                friendSheetState = false
                profileSheetState = false
                settingsSheetState = false
            }),
        SheetItem(
            "Crew", R.drawable.friends, friendSheetState,
            onShow = {
                friendSheetState = true
                activitySheetState = false
                profileSheetState = false
                settingsSheetState = false
            }),
        SheetItem(
            "My Profile", R.drawable.account, profileSheetState,
            onShow = {
                profileSheetState = true
                activitySheetState = false
                friendSheetState = false
                settingsSheetState = false
            }),
        SheetItem(
            "Settings", R.drawable.settings, settingsSheetState,
            onShow = {
                settingsSheetState = true
                profileSheetState = false
                activitySheetState = false
                friendSheetState = false
            })
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState.value) },
        bottomBar = {
            NavigationBar {
                sheetItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = item.state,
                        onClick = item.onShow,
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = "${item.label} icon"
                            )
                        },
                        label = {
                            Text(text = item.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        if (activitySheetState) ActivityBottomSheet({ activitySheetState = false }, viewModel)
        if (friendSheetState) FriendBottomSheet({ friendSheetState = false }, viewModel)
        if (profileSheetState) ProfileBottomSheet(
            { profileSheetState = false },
            viewModel,
            navController
        )
        if (settingsSheetState) SettingsBottomSheet({ settingsSheetState = false })
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
                PlaceSearchBar { place ->
                    selectedLocation = place
                    Log.d("HomeScreen", "Selected location: $selectedLocation")
                }

            }
        }
    }
}