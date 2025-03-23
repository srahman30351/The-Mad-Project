package com.example.themadproject.view.entity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.model.data.User
import com.example.themadproject.view.Screen
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.R
import com.example.themadproject.view.entity.profile.ProfileCard

@Composable
fun UserForm(
    username: MutableState<String>,
    password: MutableState<String>,
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    phoneNumber: MutableState<String>,
    imageUrl: MutableState<String>,
    viewModel: StaySafeViewModel,
    navController: NavController,
    onVerify: (User) -> Unit,
    modifier: Modifier,
) {
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileCard(
            username.value,
            firstName.value,
            lastName.value,
            phoneNumber.value,
            imageUrl.value,
            viewModel,
            onUpload = { imageUrl.value = it }
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                        onClick = { showPassword = !showPassword })
                )
            },
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.width(200.dp), onClick = {
                onVerify(
                    User(
                    username.value,
                    firstName.value,
                    lastName.value,
                    password.value,
                    phoneNumber.value,
                    imageUrl.value
                    )
                )
            }
        ) {
            Text(text = "Create Account")
        }
        TextButton(onClick = {
            navController.navigate(Screen.LoginScreen.route)
        }) {
            Text(text = "Go back to login")
        }

    }
}