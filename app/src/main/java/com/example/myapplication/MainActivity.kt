package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.HomeScreen
import com.example.myapplication.view.LoginScreen
import com.example.myapplication.view.SettingsScreen
import com.example.myapplication.view.navigation.NavItem
import com.example.myapplication.view.navigation.Screen
import com.example.myapplication.view.UserScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var selectedIndex by remember { mutableIntStateOf(0) }
            val navController = rememberNavController()
            val navItems = listOf(
                NavItem("Home", Icons.Default.Home, Screen.HomeScreen.route),
                NavItem("Friends", Icons.Default.Person, Screen.UserScreen.route),
                NavItem("Settings", Icons.Default.Settings, Screen.SettingsScreen.route)
            )

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar {
                        navItems.forEachIndexed { index, navItem ->
                            NavigationBarItem(
                                selected = selectedIndex == index,
                                onClick = {
                                    selectedIndex = index
                                          navController.navigate(navItem.route)
                                          },
                                icon = {
                                    Icon(imageVector = navItem.icon, contentDescription = "${navItem.label} icon")
                                },
                                label = {
                                    Text(text = navItem.label)
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.HomeScreen.route,
                    modifier = Modifier.padding(innerPadding),
                    builder= {
                    composable(Screen.HomeScreen.route)
                    {
                        HomeScreen(navController)
                    }
                    composable(Screen.UserScreen.route)
                    {
                        UserScreen(navController)
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




