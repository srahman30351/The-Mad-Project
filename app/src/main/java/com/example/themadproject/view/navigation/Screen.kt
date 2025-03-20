package com.example.myapplication.view.navigation

sealed class Screen(val route: String) {
    object MapScreen: Screen("map_screen")
    object LoginScreen : Screen("login_screen")
}