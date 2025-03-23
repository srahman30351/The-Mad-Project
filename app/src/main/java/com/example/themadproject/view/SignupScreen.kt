package com.example.themadproject.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.model.data.User
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.R
import com.example.themadproject.view.entity.UserForm
import kotlin.text.isBlank

@OptIn(ExperimentalMaterial3Api::class)
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
                        viewModel.findUser(it.UserUsername, { navController.navigate(Screen.MainScreen.route) })
                    })
                }
            }
            viewModel.findUser(it.UserUsername, onResult = handleResult)
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState.value) },
        topBar = {
            TopAppBar(title = { Text("Sign up") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.LoginScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back arrow",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserForm(username, password, firstName, lastName, phoneNumber, imageUrl, viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.width(200.dp),
                onClick = {
                    verifySignup(User(username.value, firstName.value, lastName.value, password.value, phoneNumber.value, imageUrl.value)
                    )
                }
            ) {
                Text(text = "Create Account")
            }
        }
    }
}