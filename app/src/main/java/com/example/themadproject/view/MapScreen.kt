package com.example.themadproject.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.myapplication.view.navigation.MapBackground
import com.example.myapplication.view.navigation.Screen
import com.example.myapplication.view.navigation.SheetItem
import com.example.themadproject.R

@Composable
fun MapScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    showSnackbar: suspend (String, String) -> Unit
) {
    var friendSheetState by remember { mutableStateOf(false) }
    var activitySheetState by remember { mutableStateOf(false) }
    var profileSheetState by remember { mutableStateOf(false) }


    val sheetItems = listOf(
        SheetItem(
            "Crewmates", R.drawable.friends, friendSheetState,
            onShow = {
                friendSheetState = true
                activitySheetState = false
                profileSheetState = false
            }),
        SheetItem(
            "Planned Journeys", R.drawable.travel, activitySheetState,
            onShow = {
                activitySheetState = true
                friendSheetState = false
                profileSheetState = false
            }),
        SheetItem(
            "My Profile", R.drawable.account, profileSheetState,
            onShow = {
                profileSheetState = true
                activitySheetState = false
                friendSheetState = false
            })
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.SettingsScreen.route)},
                content = {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            )
        }
    ) { innerPadding ->
        if (friendSheetState) UserBottomSheet({ friendSheetState = false })
        if (activitySheetState) ActivityBottomSheet({ activitySheetState = false })
        if (profileSheetState) ProfileBottomSheet({ profileSheetState = false })
        MapBackground(modifier = Modifier.fillMaxSize().padding(innerPadding))
    }
}