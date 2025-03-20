package com.example.themadproject

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.LoginScreen
import com.example.myapplication.view.navigation.Screen
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.ui.theme.TheMADProjectTheme
import com.example.themadproject.view.MapScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheMADProjectTheme {
                val navController = rememberNavController()
                val viewModel = StaySafeViewModel()
                NavHost(
                    navController = navController,
                    startDestination = Screen.LoginScreen.route,
                    builder = {
                        composable(Screen.MapScreen.route)
                        {
                            MapScreen(navController, viewModel)
                        }
                        composable(Screen.LoginScreen.route)
                        {
                            LoginScreen(navController, viewModel)
                        }
                    })
            }
        }
    }
}