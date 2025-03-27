package com.example.themadproject.view

import PlaceSearchBar
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.network.HttpException
import com.example.myapplication.model.data.Activity
import com.example.myapplication.model.data.User
import com.example.myapplication.view.navigation.MapBackground
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.view.entity.sheet.SheetItem
import com.example.themadproject.R
import com.example.themadproject.model.api.RouteUtils
import com.example.themadproject.view.entity.sheet.ActivityBottomSheet
import com.example.themadproject.view.entity.sheet.FriendBottomSheet
import com.example.themadproject.view.entity.sheet.ProfileBottomSheet
import com.example.themadproject.view.entity.sheet.SettingsBottomSheet
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: StaySafeViewModel
) {
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var activitySheetState by remember { mutableStateOf(false) }
    var friendSheetState by remember { mutableStateOf(false) }
    var profileSheetState by remember { mutableStateOf(false) }
    var settingsSheetState by remember { mutableStateOf(false) }
    val currentUser = viewModel.user.collectAsState().value
    val currentActivity = viewModel.activities.collectAsState().value
    var selectedActivity by remember { mutableStateOf<Activity?>(null) }
    val routeLine = remember { mutableStateOf<PolylineOptions?>(null) }
    val route2Line = remember { mutableStateOf<PolylineOptions?>(null) }
    val estTime = remember { mutableStateOf<Long>(0L) }
    val estTime2 = remember { mutableStateOf<Long>(0L) }
    var startLocation by remember { mutableStateOf<LatLng?>(null) }
    var endLocation by remember { mutableStateOf<LatLng?>(null) }
    val users = viewModel.users.collectAsState().value
    var selectedFriend by remember { mutableStateOf<User?>(null) }
    var profileState by remember { mutableStateOf(false) }
    var isActivityStarted by remember { mutableStateOf(false) }
    var isActivityPaused by remember { mutableStateOf(false) }
    var isActivityCompleted by remember { mutableStateOf(false) }

    val activityButtonState = if (isActivityStarted) {
        if (isActivityPaused) "Resume" else "Pause"
    } else {
        "Start"
    }

    val sheetItems = listOf(
        SheetItem(
            "My Profile", R.drawable.account, profileSheetState,
            onShow = {
                profileSheetState = true
                activitySheetState = false
                friendSheetState = false
                settingsSheetState = false
            }),
        SheetItem(
            "Activities", R.drawable.travel, activitySheetState,
            onShow = {
                activitySheetState = true
                friendSheetState = false
                profileSheetState = false
                settingsSheetState = false
            }),
        SheetItem(
            "My Friends", R.drawable.contacts, friendSheetState,
            onShow = {
                friendSheetState = true
                activitySheetState = false
                profileSheetState = false
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
                },
                navController = navController
            )
        }
        if (friendSheetState) FriendBottomSheet({ friendSheetState = false }, viewModel)
        LaunchedEffect(profileSheetState) {
            if (profileSheetState) {
                Log.d(
                    "MainScreen",
                    "Launching ProfileBottomSheet for: ${selectedFriend?.UserUsername}"
                )
            }
        }
        if (profileSheetState) {
            val showUser = selectedFriend ?: currentUser
            if (showUser != null) {
                if (selectedFriend != null) {
                    Log.d("MainScreen", "Profile sheet opened for: ${selectedFriend?.UserUsername}")
                }
                ProfileBottomSheet(
                    onDismiss = { profileSheetState = false },
                    viewModel = viewModel,
                    navController = navController,
                    user = showUser,
                    isCurrentUser = selectedFriend?.UserID != currentUser?.UserID
                )
            } else {
                Log.e("MainScreen", "Error: showUser is NULL, cannot open ProfileBottomSheet")
            }
        }

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


                    val endPoint = if (activity.ActivityFromName == activity.ActivityToName) {
                        LatLng(user.UserLatitude + 0.001, user.UserLongitude + 0.001)
                    } else {
                        getCords(
                            context = LocalContext.current,
                            locationName = activity.ActivityToName ?: "Flexible"
                        ) ?: LatLng(user.UserLatitude, user.UserLongitude)
                    }
                    Log.d("MainScreen", "End Point: $endPoint")
                    if (startPoint == endPoint) {
                        Log.e("MainScreen", "Start and end locations are the same")
                        return@let
                    }
                    val apiKey = "AIzaSyBpRd8pMrcC34T4riIish0azmEmyu8QreQ"
                    LaunchedEffect(key1 = selectedActivity) {
                        val requestUrl =
                            "https://routes.googleapis.com/v2:computeRoutes?origin=${startPoint.latitude},${startPoint.longitude}&destination=${endPoint.latitude},${endPoint.longitude}&key=$apiKey"
                        try {
                            val route1 = RouteUtils.getRoute(
                                LatLng(user.UserLatitude, user.UserLongitude),
                                startPoint,
                                apiKey
                            )
                            val route2 = RouteUtils.getRoute(startPoint, endPoint, apiKey)
                            val firstRoute1 = route1.routes.firstOrNull()
                            val firstRoute2 = route2.routes.firstOrNull()


                            val duration1 =
                                firstRoute1?.duration?.removeSuffix("s")?.toIntOrNull()?.toLong()
                                    ?: 0L
                            val duration2 =
                                firstRoute2?.duration?.removeSuffix("s")?.toIntOrNull()?.toLong()
                                    ?: 0L
                            estTime.value = duration1
                            estTime2.value = duration2
                            //val route1Polyline = RouteUtils.decodePolyline(firstRoute1.polyline.encodedPolyline)
                            routeLine.value =
                                firstRoute1?.polyline?.encodedPolyline?.let { encoded ->
                                    PolylineOptions().addAll(RouteUtils.decodePolyline(encoded))
                                        .color(Color.Blue.toArgb()).width(8f)
                                }
                            route2Line.value =
                                firstRoute2?.polyline?.encodedPolyline?.let { encoded ->
                                    PolylineOptions().addAll(RouteUtils.decodePolyline(encoded))
                                        .color(Color.Red.toArgb()).width(8f)
                                }

                            startLocation = startPoint
                            endLocation = endPoint
                        } catch (e: HttpException) {
                            Log.e("MainScreen", "HTTP error while fetching route: ${e.message}")
                        } catch (e: Exception) {
                            Log.e("MainScreen", "General error while fetching route: ${e.message}")
                        }
                    }
                }
                MapBackground(
                    modifier = Modifier
                        .matchParentSize(),
                    user = user,
                    selectedLocation = selectedLocation,
                    startPoint = startLocation,
                    endPoint = endLocation,
                    route1Polyline = routeLine.value,
                    route2Polyline = route2Line.value,
                    estTime = "${estTime.value / 60}",
                    estTime2 = "${estTime2.value / 60}",
                    friendsList = users,
                    viewModel = viewModel,
                    selectedFriend = selectedFriend,
                    profileState = profileState,
                    onProfileSheetChange = { newState, friend ->
                        Log.d(
                            "MainScreen",
                            "Profile sheet state: $newState, Selected Friend: ${friend.UserUsername}"
                        )
                        profileState = newState
                        selectedFriend = friend
                    }

                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                PlaceSearchBar(viewModel) { place, query ->
                    selectedLocation = place
                }

            }
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                if (isActivityStarted) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(onClick = { isActivityPaused = !isActivityPaused }) {
                            Text(if (isActivityPaused) "Resume" else "Pause")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = { isActivityCompleted = true }) {
                            Text("Complete")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = {
                            isActivityStarted = false
                            isActivityCompleted = false
                            isActivityPaused = false
                        }) {
                            Text("Stop")
                        }
                    }
                } else {
                    Button(onClick = { isActivityStarted = true }) {
                        Text("Start Activity")
                    }
                }
            }
        }
    }
}

fun getCords(context: Context, locationName: String): LatLng? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: MutableList<Address>? = geocoder.getFromLocationName(locationName, 1)
    return if (!addresses.isNullOrEmpty()) {
        val address = addresses[0]
        LatLng(address.latitude, address.longitude)
    } else {
        null
    }
}