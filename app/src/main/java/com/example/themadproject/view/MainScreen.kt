package com.example.themadproject.view

import PlaceSearchBar
import android.content.Context
import android.location.Address
import android.location.Geocoder
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.myapplication.model.data.Activity
import com.example.myapplication.model.data.User
import com.example.myapplication.view.navigation.MapBackground
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.view.entity.sheet.SheetItem
import com.example.themadproject.R
import com.example.themadproject.model.api.RouteUtils
import com.example.themadproject.model.api.RouteUtils.getRoute
import com.example.themadproject.view.entity.sheet.ActivityBottomSheet
import com.example.themadproject.view.entity.sheet.FriendBottomSheet
import com.example.themadproject.view.entity.sheet.ProfileBottomSheet
import com.example.themadproject.view.entity.sheet.SettingsBottomSheet
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import java.util.Locale

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
    val currentUser= viewModel.user.collectAsState().value
    val currentAcitivity = viewModel.activities.collectAsState().value
    var selectedActivity by remember { mutableStateOf<Activity?>(null) }
    val routeLine = remember { mutableStateOf<PolylineOptions?>(null) }
    val route2Line = remember { mutableStateOf<PolylineOptions?>(null) }
    val estTime = remember { mutableStateOf("") }
    val estTime2 = remember { mutableStateOf("") }
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
        if (activitySheetState) {
            ActivityBottomSheet(
                onDismiss = { activitySheetState = false },
                viewModel = viewModel,
                onActivitySelected = { activities ->
                    selectedActivity = activities
                    activitySheetState = false
                }
            )
        }
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
            currentUser?.let { user ->
                selectedActivity?.let { activity ->
                    val startPoint = getCords(
                        context = LocalContext.current,
                        locationName = activity.ActivityFromName ?: "Your Location"
                    ) ?: LatLng(user.UserLatitude, user.UserLongitude)

                    val endPoint = getCords(
                        context = LocalContext.current,
                        locationName = activity.ActivityFromName ?: "Flexiable"
                    ) ?: LatLng(user.UserLatitude, user.UserLongitude)
                    val apiKey = "AIzaSyBpRd8pMrcC34T4riIish0azmEmyu8QreQ"
                    LaunchedEffect(key1 = selectedActivity) {
                        val route1 = RouteUtils.getRoute(
                            LatLng(user.UserLatitude, user.UserLongitude),
                            startPoint,
                            apiKey
                        )
                        val route2 = RouteUtils.getRoute(startPoint, endPoint, apiKey)
                        val route1Polyline = RouteUtils.decodePolyline(route1.routes[0].overviewPolyLine.encondedPolyline)
                    }
                }
            MapBackground(
                modifier = Modifier
                    .matchParentSize(),
                user = user,
                selectedLocation = selectedLocation
            )
                Log.d("HomeScreen", "Users location$user")
        }
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
fun getCords(context: Context, locationName: String) : LatLng? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: MutableList<Address>? = geocoder.getFromLocationName(locationName, 1)
    return if (!addresses.isNullOrEmpty()) {
        val address = addresses[0]
        LatLng(address.latitude, address.longitude)
    } else {
        null
    }
}