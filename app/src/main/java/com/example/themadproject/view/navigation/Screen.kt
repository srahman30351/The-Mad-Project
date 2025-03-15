package com.example.myapplication.view.navigation

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object UserScreen : Screen("user_screen")
    object LoginScreen : Screen("login_screen")
    object SettingsScreen : Screen("settings_screen")
}