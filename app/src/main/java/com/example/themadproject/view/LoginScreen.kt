package com.example.myapplication.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.model.data.User
import com.example.myapplication.view.navigation.Screen
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: StaySafeViewModel
) {
    val users = viewModel.users.collectAsState().value
    var username = remember { mutableStateOf("FreedomFighter222") }
    var password = remember { mutableStateOf("123123123") }
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
                onClick = {
                        verifyLogin(
                            username = username,
                            password = password,
                            users = users,
                            showSnackbar = { message, action ->
                                viewModel.showSnackbar(
                                    message,
                                    action
                                )
                            },
                            onLogin = { account ->
                                viewModel.setUser(account)
                                navController.navigate(Screen.MainScreen.route)
                            },
                        )
                }) {
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
    users: List<User>,
    showSnackbar: (String, String) -> Unit,
    onLogin: (User) -> Unit
) {
    if (username.value.isBlank() || password.value.isBlank()) {
        showSnackbar("Please fill in the fields", "Login error")
        return
    }
    val account = users.find { it.UserUsername == username.value }
    if (account == null) {
        showSnackbar("Account doesn't exist", "error")
        username.value = ""
        password.value = ""
        return
    } else if (account.UserPassword != password.value) {
        showSnackbar("Password is incorrect", "error")
        password.value = ""
        return
    } else {
        showSnackbar("Successfully logged in!", "action")
        onLogin(account)
        return
    }
}

