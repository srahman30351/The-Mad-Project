package com.example.themadproject.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
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
        remember { mutableStateOf("https://t3.ftcdn.net/jpg/08/05/28/22/360_F_805282248_LHUxw7t2pnQ7x8lFEsS2IZgK8IGFXePS.jpg") }
    var password = remember { mutableStateOf("123123123") }
    var showPassword by remember { mutableStateOf(false) }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri })

    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState.value) }) { paddingValues ->
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
                    verifySignup(
                        username.value,
                        firstName.value,
                        lastName.value,
                        password.value,
                        phoneNumber.value,
                        imageUrl.value,
                        viewModel,
                        navController
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
            TextButton(onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text(text = "Pick an image")
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
        onClick = { }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(Modifier.size(150.dp)) {
                AsyncImage(
                    model = imageUrl,
                    modifier = Modifier.aspectRatio(1f / 1f),
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
                    text = phoneNumber, overflow = TextOverflow.Ellipsis, maxLines = 1

                )
            }
        }
    }
}

private fun verifySignup(
    username: String,
    firstName: String,
    lastName: String,
    password: String,
    phoneNumber: String,
    imageUrl: String,
    viewModel: StaySafeViewModel,
    navController: NavController
) {
    //The StaySafe API error response will take care of all other validations to meet the POST requirements and will send a the issue in a snackBar to the user
    if (username.isBlank() || firstName.isBlank() || lastName.isBlank() || password.isBlank() || phoneNumber.isBlank()) {
        viewModel.showSnackbar("Please fill in the fields", "Error")
        return
    }

    var handleResult: (Boolean) -> Unit = { isExist ->
        if (isExist) {
            viewModel.showSnackbar("Username already taken", "Error")
        } else {
            viewModel.setUser(User(username, firstName, lastName, password, phoneNumber, imageUrl))
            navController.navigate(Screen.MainScreen.route)
            viewModel.showSnackbar(
                "Account successfully created!", "Success"
            )
        }
    }
    viewModel.findUser(username, handleResult)
}


