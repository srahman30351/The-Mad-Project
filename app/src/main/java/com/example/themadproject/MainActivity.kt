package com.example.themadproject

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.LoginScreen
import com.example.themadproject.view.Screen
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.ui.theme.TheMADProjectTheme
import com.example.themadproject.view.AddActivityScreen
import com.example.themadproject.view.EditProfileScreen
import com.example.themadproject.view.HomeScreen
import com.example.themadproject.view.SignupScreen
import com.example.themadproject.view.entity.UserListScreen
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyBpRd8pMrcC34T4riIish0azmEmyu8QreQ")
        }
        enableEdgeToEdge()
        setContent {
            TheMADProjectTheme {
                val navController = rememberNavController()
                val viewModel = StaySafeViewModel()
                NavHost(
                    navController = navController,
                    startDestination = Screen.LoginScreen.route,
                    builder = {
                        composable(Screen.HomeScreen.route)
                        {
                            HomeScreen(navController, viewModel)
                        }
                        composable(Screen.EditProfile.route)
                        {
                            EditProfileScreen(navController, viewModel)
                        }
                        composable(Screen.AddActivity.route)
                        {
                            AddActivityScreen(navController, viewModel)
                        }
                        composable(Screen.LoginScreen.route)
                        {
                            LoginScreen(navController, viewModel)
                        }
                        composable(Screen.SignupScreen.route)
                        {
                            SignupScreen(navController, viewModel)
                        }
                        composable(Screen.UserList.route)
                        {
                            UserListScreen(navController, viewModel)
                        }
                    })
            }
        }
    }
}