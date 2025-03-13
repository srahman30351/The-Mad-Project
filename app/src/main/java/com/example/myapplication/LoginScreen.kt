package com.example.myapplication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController){
    var text by remember { mutableStateOf("Enter Username") }
    var text2 by remember { mutableStateOf("Enter Password") }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login")
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(16.dp) )
        OutlinedTextField(
                value = text2,
        onValueChange = { text2 = it },
        label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp) )

        Button(onClick = {
            navController.navigate("home_screen")
        }) {
            Text(text = "Login")
        }


    }

}

