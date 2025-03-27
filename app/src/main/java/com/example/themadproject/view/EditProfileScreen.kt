package com.example.themadproject.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.model.data.User
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.R
import com.example.themadproject.model.api.StaySafe
import com.example.themadproject.view.entity.UserForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: StaySafeViewModel,
) {
    viewModel.user.collectAsState().value?.let { user ->
        var username = remember { mutableStateOf(user.UserUsername) }
        var password = remember { mutableStateOf(user.UserPassword) }
        var firstName = remember { mutableStateOf(user.UserFirstname) }
        var lastName = remember { mutableStateOf(user.UserLastname) }
        var phoneNumber = remember { mutableStateOf(user.UserPhone) }
        var imageUrl = remember { mutableStateOf(user.UserImageURL) }

        var deleteAlert by remember { mutableStateOf(false) }

//Handler ---------------------------------------------------------------------------------

        var verifyEdit: (User) -> Unit = { newUser ->
            //The StaySafe API error response will take care of all other validations to meet the POST requirements and will send a the issue in a snackBar to the user
            if (newUser.UserFirstname.isBlank() || newUser.UserFirstname.isBlank() || newUser.UserLastname.isBlank() || newUser.UserPassword.isBlank() || newUser.UserPhone.isBlank()) {
                viewModel.showSnackbar("Please fill in the fields", "Error")
            } else {
                viewModel.getUserUsername(newUser.UserUsername) { user ->
                    if (user != null && (newUser.UserUsername.lowercase() != user.UserUsername.lowercase())) {
                        viewModel.showSnackbar("Username already taken", "Error")
                    } else {
                        viewModel.putData(newUser) {
                            viewModel.setUser(newUser)
                            navController.navigate(Screen.HomeScreen.route)
                        }
                    }
                }
            }
        }

        var handleDelete: () -> Unit = {
            viewModel.deleteData(StaySafe.User, user.UserID) {
                navController.navigate(Screen.LoginScreen.route)
            }
        }
//Composable ---------------------------------------------------------------------------------

        if(deleteAlert) {
            DeleteAlertDialog(
                onDismiss = { deleteAlert = false },
                onDelete = handleDelete
            )
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState.value) },
            topBar = {
                TopAppBar(title = { Text("Edit profile") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back arrow",
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { deleteAlert = true }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.delete_forever),
                                contentDescription = "Delete Forever",
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
                        verifyEdit(user.copy(username.value, firstName.value, lastName.value, password.value, phoneNumber.value, imageUrl.value))
                    }
                ) {
                    Text(text = "Edit Account")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAlertDialog(onDismiss: () -> Unit, onDelete: () -> Unit) {
    BasicAlertDialog( onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "User Confirmation",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    "Are you sure you want to delete your account?",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Row {
                    Button(onClick = onDelete,
                        modifier = Modifier.weight(1F),
                    ) { Text(text = "Yes") }

                    Spacer(Modifier.width(10.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                        ),
                        modifier = Modifier.weight(1F)
                    ) { Text("No") }
                }
            }
        }
    }
}