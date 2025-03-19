package com.example.themadproject

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.LoginScreen
import com.example.myapplication.view.SettingsScreen
import com.example.myapplication.view.navigation.MapBackground
import com.example.myapplication.view.navigation.SheetItem
import com.example.myapplication.view.navigation.Screen
import com.example.themadproject.ui.theme.TheMADProjectTheme
import com.example.themadproject.view.ActivityBottomSheet
import com.example.themadproject.view.UserBottomSheet

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheMADProjectTheme {
                val navController = rememberNavController()
                var friendSheetState by remember { mutableStateOf(false) }
                var activitySheetState by remember { mutableStateOf(false) }

                val sheetItems = listOf(
                    SheetItem(
                        "Crewmates", R.drawable.friends, friendSheetState,
                        onShow = { friendSheetState = true },
                        onDismiss = { friendSheetState = false }),
                    SheetItem(
                        "Planned Journeys", R.drawable.travel, activitySheetState,
                        onShow = { activitySheetState = true },
                        onDismiss = { activitySheetState = false }
                    )
                )
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            sheetItems.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = item.state,
                                    onClick = item.onShow,
                                    icon = {
                                        Icon(painter = painterResource(item.icon),
                                            contentDescription = "${item.label} icon")
                                    },
                                    label = {
                                        Text(text = item.label)
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.LoginScreen.route,
                        modifier = Modifier.padding(innerPadding),
                        builder= {
                            composable(Screen.MapScreen.route)
                            {
                                if (friendSheetState) {
                                    UserBottomSheet(
                                        onDismiss = { friendSheetState = false }
                                    )
                                }
                                if (activitySheetState) {
                                    ActivityBottomSheet(
                                        onDismiss = { activitySheetState = false }
                                    )
                                }
                                MapBackground(modifier = Modifier.fillMaxSize())
                            }
                            composable(Screen.SettingsScreen.route)
                            {
                                SettingsScreen(navController)
                            }
                            composable(Screen.LoginScreen.route)
                            {
                                LoginScreen(navController)
                            }
                        })
                }
            }
        }
    }
}