package com.example.themadproject.view

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
    object EditProfile : Screen("edit_profile_screen")
}