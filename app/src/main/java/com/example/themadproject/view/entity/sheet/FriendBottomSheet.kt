package com.example.themadproject.view.entity.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.StaySafeViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.model.data.User
import com.example.themadproject.R
import com.example.themadproject.view.Screen
import com.google.android.material.chip.Chip


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendBottomSheet(
    onDismiss: () -> Unit,
    navController: NavController,
    viewModel: StaySafeViewModel
) {
    val contactUsers = viewModel.contactUsers.collectAsState().value
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.navigationBarsPadding(),
        sheetState = sheetState,
        containerColor = Color(0xfff8fbfc),
        scrimColor = Color.Transparent,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            FriendList(contactUsers, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendList(
    contactUsers: List<User>,
    navController: NavController
) {
    TopAppBar(
        title = { Text("My Friends") },
        actions = {
            AssistChip(
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = { navController.navigate(Screen.UserList.route) },
                label = { Text("Find friends") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.add_friend),
                        contentDescription = "Add Friend",
                    )
                }
            )
        }
    )
    LazyColumn(
        modifier = Modifier.height(500.dp)
    ) {
        if (contactUsers.isNotEmpty()) {
            items(contactUsers) { friend ->
                FriendCard(friend)
                HorizontalDivider()
            }
        } else {
            item { Text("You have no friends") }
        }
    }
}

@Composable
fun FriendCard(friend: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xff57B4BA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(Modifier.size(85.dp)) {
                AsyncImage(
                    model = friend.UserImageURL,
                    modifier = Modifier
                        .aspectRatio(1f / 1f),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
            ) {
                Text(
                    text = friend.UserUsername,
                    fontWeight = FontWeight.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Text(
                    text = "${friend.UserFirstname} ${friend.UserLastname}",
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Text(
                    text = friend.UserPhone,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}