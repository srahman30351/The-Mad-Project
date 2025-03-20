package com.example.myapplication.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val coroutineScope = rememberCoroutineScope()
    var username by remember { mutableStateOf("dantheman123") }
    var password by remember { mutableStateOf("password1") }
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
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
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

            Button(onClick = {
                coroutineScope.launch {
                    verifyLogin(
                        username = username,
                        password = password,
                        users = users,
                        showSnackbar = { message, action -> viewModel.showSnackbar(message,action)},
                        onLogin = { account ->
                            viewModel.getUser(account)
                            navController.navigate(Screen.MapScreen.route)
                        },
                    )
                }
            }) {
                Text(text = "Login")
            }
        }
    }
}

private fun verifyLogin(
    username: String,
    password: String,
    users: List<User>,
    showSnackbar: (String, String) -> Unit,
    onLogin: (User) -> Unit
) {
    if (username.isBlank() || password.isBlank()) {
        showSnackbar("Please fill in the fields", "Login error")
        return
    }
    val account = users.find { it.UserUsername == username }
    if (account == null) {
        showSnackbar("Account doesn't exist", "Login error")
        return
    } else if (account.UserPassword != password) {
        showSnackbar("Password is incorrect", "Login error")
        return
    } else {
        showSnackbar("Logging into account", "Login success")
        onLogin(account)
        return
    }
}

