package com.example.themadproject.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.model.data.User
import com.example.myapplication.view.navigation.Screen
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.R

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: StaySafeViewModel,
) {
    val users = viewModel.users.collectAsState().value
    var username = remember { mutableStateOf("FreedomFighter222") }
    var firstName = remember { mutableStateOf("Johnson") }
    var lastName = remember { mutableStateOf("Roughman") }
    var phoneNumber = remember { mutableStateOf("+44 7911 987654") }
    var imageUrl =
        remember { mutableStateOf("https://i.pinimg.com/474x/85/b4/06/85b4066060120a0ee602815af9da2d0d.jpg") }
    var password = remember { mutableStateOf("123123123") }
    var showPassword by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState.value) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ProfileCard(
                username.value,
                firstName.value,
                lastName.value,
                phoneNumber.value,
                imageUrl.value
            )
            OutlinedTextField(
                value = firstName.value,
                onValueChange = { firstName.value = it },
                label = { Text("First Name") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = lastName.value,
                onValueChange = { lastName.value = it },
                label = { Text("Last Name") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = phoneNumber.value,
                onValueChange = { phoneNumber.value = it },
                label = { Text("Phone Number") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = imageUrl.value,
                onValueChange = { imageUrl.value = it },
                label = { Text("Image Url") },
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
                    val user = User(
                        username.value,
                        firstName.value,
                        lastName.value,
                        password.value,
                        phoneNumber.value,
                        imageUrl.value
                    )
                    viewModel.createUser(
                        user = user,
                        onCreate = {
                            viewModel.setUser(user)
                            navController.navigate(Screen.LoginScreen.route)
                        }
                    )

                }) {
                Text(text = "Create Account")
            }
            TextButton(onClick = {
                navController.navigate(Screen.LoginScreen.route)
            }) {
                Text(text = "Go back to login")
            }
        }
    }
}

@Composable
fun ProfileCard(
    username: String,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    imageUrl: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffA3C9A8)
        ),
        onClick = { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(Modifier.size(150.dp)) {
                AsyncImage(
                    model = imageUrl,
                    modifier = Modifier
                        .aspectRatio(1f / 1f),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = username,
                    fontWeight = FontWeight.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = "${firstName} ${lastName}",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = phoneNumber,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1

                )
            }
        }
    }
}

private fun verifySignup(
    username: MutableState<String>,
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    password: MutableState<String>,
    phoneNumber: MutableState<String>,
    imageUrl: MutableState<String>,
    onSignup: (User) -> Unit
) {
    onSignup(
        User(
            username.value,
            firstName.value,
            lastName.value,
            password.value,
            phoneNumber.value,
            imageUrl.value,
            0.0,
            0.0,
            0.0
        )
    )
}


