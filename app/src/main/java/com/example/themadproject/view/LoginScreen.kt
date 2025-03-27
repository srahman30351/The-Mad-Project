package com.example.myapplication.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.themadproject.view.Screen
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.R

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: StaySafeViewModel
) {
    var username = remember { mutableStateOf("aishaahmed") }
    var password = remember { mutableStateOf("Password") }
    var showPassword by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState.value) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Login")
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                trailingIcon = {
                    Icon(
                        painter = if (showPassword) painterResource(R.drawable.visibility)
                        else painterResource(R.drawable.visibility_off),
                        contentDescription = null,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { showPassword = !showPassword }
                        )
                    )
                },
                visualTransformation = if (showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { verifyLogin(username, password, viewModel, navController) }
            ) {
                Text(text = "Login")
            }
            TextButton(onClick = {
                navController.navigate(Screen.SignupScreen.route)
            }) {
                Text(text = "Create Account")
            }
        }
    }
}

private fun verifyLogin(
    username: MutableState<String>,
    password: MutableState<String>,
    viewModel: StaySafeViewModel,
    navController: NavController
) {
    if (username.value.isBlank()) {
        viewModel.showSnackbar("Please fill in the username field", "Error")
        return
    }
    viewModel.getUserUsername(username.value) { user ->
        if (user == null) {
            viewModel.showSnackbar("Account doesn't exist", "Error")
        } else if (user.UserPassword != password.value) {
            viewModel.showSnackbar("Password is incorrect", "Error")
            password.value = ""
        } else {
            viewModel.setUser(user)
            viewModel.showSnackbar("Welcome ${username.value}!", "Login Success")
            navController.navigate(Screen.HomeScreen.route)
        }
    }
}

