package com.example.themadproject.view

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
    object EditProfile : Screen("edit_profile_screen")
    object AddActivity : Screen("add_activity_screen")
    object UserList : Screen("user_list_screen")
}