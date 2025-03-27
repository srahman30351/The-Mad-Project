package com.example.themadproject.view.entity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.model.data.User
import com.example.myapplication.viewmodel.StaySafeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleList(
    users: List<User>,
    requests: List<User>,
    onDismiss: () -> Unit,
    viewModel: StaySafeViewModel
) {
    val handleAddFriendRequest: (userID: Int) -> Unit = { userID ->
        viewModel.postFriendRequest(userID) {
            viewModel.showSnackbar("Successfully sent a friend request!", "Success")
        }
    }
    
    TopAppBar(
        title = { Text("Add Friend") },
        navigationIcon = {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back arrow",
                )
            }
        }
    )
    LazyColumn(
        modifier = Modifier.height(500.dp)
    ) {
        items(requests) { user ->
            FriendRequestCard(
                user,
                onDeny = {},
                onAccept = {}
            )
        }
        items(users) { user ->
            AddFriendCard(user, handleAddFriendRequest)
        }
    }
}

@Composable
fun FriendRequestCard(requester: User, onAccept: () -> Unit, onDeny: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffE28743)
        ),
        onClick = { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(Modifier.size(85.dp)) {
                AsyncImage(
                    model = requester.UserImageURL,
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
                    text = "Friend Request: ${requester.UserUsername}",
                    fontWeight = FontWeight.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Text(
                    text = "${requester.UserFirstname} ${requester.UserLastname}",
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                TextButton(onClick = onAccept
                ) {
                    Text(text = "Accept Friend Request",
                        color = Color.Blue)
                }
                TextButton(onClick = onDeny
                ) {
                    Text(
                        text = "Deny Friend Request",
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun AddFriendCard(friend: User, onAddFriendRequest: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xff74A4E4)
        ),
        onClick = { }
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
                TextButton(onClick = { onAddFriendRequest(friend.UserID) }
                ) {
                    Text(text = "Send friend request")
                }
            }
        }
    }
}