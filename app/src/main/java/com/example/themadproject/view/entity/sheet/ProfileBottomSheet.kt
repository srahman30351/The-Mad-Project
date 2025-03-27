package com.example.themadproject.view.entity.sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.model.data.User
import com.example.themadproject.view.Screen
import com.example.myapplication.viewmodel.StaySafeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    onDismiss: () -> Unit,
    viewModel: StaySafeViewModel,
    navController: NavController
) {
    val profile = viewModel.user.collectAsState().value
    if (profile != null) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            scrimColor = Color.Transparent,
            onDismissRequest = onDismiss
        ) {
            ProfileCard(profile)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
            ) {
                Button(onClick = {
                    navController.navigate(Screen.EditProfile.route)
                }) {
                    Text(text = "Edit Profile")
                }

                OutlinedButton(
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    onClick = {
                        viewModel.setUser(null)
                        viewModel.showSnackbar("Successfully signed out!","Success")
                        navController.navigate(Screen.LoginScreen.route)
                }) {
                    Text(text = "Sign Out", color = Color.Red, fontWeight = FontWeight.Bold)
                }

            }
        }
    }
}

@Composable
fun ProfileCard(profile: User) {
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
                    model = profile.UserImageURL,
                    modifier = Modifier
                        .aspectRatio(1f / 1f),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = profile.UserUsername,
                    fontWeight = FontWeight.Black,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${profile.UserFirstname} ${profile.UserLastname}",
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = profile.UserPhone,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}