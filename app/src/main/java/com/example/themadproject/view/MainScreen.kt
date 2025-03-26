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
import com.example.themadproject.model.api.RouteUtils.getRoute
import com.example.themadproject.view.entity.sheet.ActivityBottomSheet
import com.example.themadproject.view.entity.sheet.FriendBottomSheet
import com.example.themadproject.view.entity.sheet.ProfileBottomSheet
import com.example.themadproject.view.entity.sheet.SettingsBottomSheet
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import okhttp3.internal.concurrent.formatDuration
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
val estTime = remember { mutableStateOf<Long>(0L) }
    val estTime2 = remember { mutableStateOf<Long>(0L) }
    var startLocation by remember { mutableStateOf<LatLng?>(null) }
    var endLocation by remember { mutableStateOf<LatLng?>(null) }
    val users = viewModel.users.collectAsState().value

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
            "Friends", R.drawable.friends, friendSheetState,
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

                    Log.d("MainScreen", "Start Point: $startPoint")

                    val endPoint = if(activity.ActivityFromName == activity.ActivityToName) {
                        Log.d("MainScreen", "Start and End Locations are the same so adjusting")
                        LatLng(user.UserLatitude + 0.001, user.UserLongitude + 0.001)
                    } else{
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
                        Log.d("MainScreen", "Requesting route URL: $requestUrl")
                        try {
                            val route1 = RouteUtils.getRoute(
                                LatLng(user.UserLatitude, user.UserLongitude),
                                startPoint,
                                apiKey
                            )
                            Log.d("MainScreen", "Route 1 Response: ${route1}")
                            val route2 = RouteUtils.getRoute(startPoint, endPoint, apiKey)
                            Log.d("MainScreen", "Route 2 Response: ${route2}")
                            val firstRoute1 = route1.routes.firstOrNull()
                            val firstRoute2 = route2.routes.firstOrNull()


                            val duration1 = firstRoute1?.duration?.removeSuffix("s")?.toIntOrNull()?.toLong() ?: 0L
                            val duration2 = firstRoute2?.duration?.removeSuffix("s")?.toIntOrNull()?.toLong() ?: 0L
                            estTime.value = duration1
                            estTime2.value = duration2
                            //val route1Polyline = RouteUtils.decodePolyline(firstRoute1.polyline.encodedPolyline)
                            routeLine.value = firstRoute1?.polyline?.encodedPolyline?.let { encoded ->
                                PolylineOptions().addAll(RouteUtils.decodePolyline(encoded)).color(Color.Blue.toArgb()).width(8f)
                            }
                            route2Line.value = firstRoute2?.polyline?.encodedPolyline?.let { encoded ->
                                PolylineOptions().addAll(RouteUtils.decodePolyline(encoded)).color(Color.Red.toArgb()).width(8f)
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
                Log.d("MapBackground", "Route1 polyline: ${routeLine.value?.points}")
                Log.d("MapBackground", "Route2 polyline: ${route2Line.value?.points}")
                Log.d("EstTime", "Estime is: ${formatDuration(estTime.value)}")
                Log.d("EstTime", "Estime2 is: ${formatDuration(estTime2.value)}")
                Log.d("MainScreen", "friendsList size: ${users.size}")


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
                estTime2 = "${estTime2.value/60}",
                friendsList = users
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