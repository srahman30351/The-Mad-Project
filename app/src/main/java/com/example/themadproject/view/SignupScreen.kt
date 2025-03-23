package com.example.themadproject.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.myapplication.model.data.User
import com.example.themadproject.view.Screen
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.view.entity.UserForm
import kotlin.text.isBlank

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: StaySafeViewModel,
) {
    var username = remember { mutableStateOf("FreedomFighter222") }
    var password = remember { mutableStateOf("123123123") }
    var firstName = remember { mutableStateOf("Johnson") }
    var lastName = remember { mutableStateOf("Roughman") }
    var phoneNumber = remember { mutableStateOf("+44 7911 987654") }
    var imageUrl =
        remember { mutableStateOf("https://t3.ftcdn.net/jpg/08/05/28/22/360_F_805282248_LHUxw7t2pnQ7x8lFEsS2IZgK8IGFXePS.jpg") }


    var verifySignup: (User) -> Unit = {
        //The StaySafe API error response will take care of all other validations to meet the POST requirements and will send a the issue in a snackBar to the user
        if (it.UserFirstname.isBlank() || it.UserFirstname.isBlank() || it.UserLastname.isBlank() || it.UserPassword.isBlank() || it.UserPhone.isBlank()) {
            viewModel.showSnackbar("Please fill in the fields", "Error")
        } else {

            var handleResult: (Boolean) -> Unit = { isExist ->
                if (isExist) {
                    viewModel.showSnackbar("Username already taken", "Error")
                } else {
                    viewModel.createUser(it, {
                        viewModel.setUser(it)
                        navController.navigate(Screen.MainScreen.route)
                        viewModel.showSnackbar(
                            "Account successfully created!", "Success"
                        )
                    })
                }
            }
            viewModel.findUser(it.UserUsername, onResult = handleResult)
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState.value) }) { paddingValues ->
        UserForm(
            username,
            password,
            firstName,
            lastName,
            phoneNumber,
            imageUrl,
            viewModel,
            navController,
            verifySignup,
            Modifier.padding(paddingValues),
        )
    }
}