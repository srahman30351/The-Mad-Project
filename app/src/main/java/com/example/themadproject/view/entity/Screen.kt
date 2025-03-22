package com.example.myapplication.view.navigation

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
}