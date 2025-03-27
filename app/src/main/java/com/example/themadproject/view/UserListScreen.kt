package com.example.themadproject.view.entity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.model.data.User
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.R
import com.example.themadproject.model.StaySafe
import com.example.themadproject.view.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavController, viewModel: StaySafeViewModel) {
    val users = viewModel.users.collectAsState().value
    val userRequests = viewModel.requestUsers.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }
    var userFilter by remember { mutableStateOf("Username") }

    val handleAddFriendRequest: (userID: Int) -> Unit = { userID ->
        viewModel.postFriendRequest(userID) {
            viewModel.showSnackbar("Successfully sent a friend request!", "Success")
        }
    }

    val handleAcceptFriendRequest: (Int, Int) -> Unit = { userID, contactID ->
        viewModel.deleteData(StaySafe.Contact, contactID) {
            viewModel.addFriend(userID, contactID) {
                viewModel.loadUserContent()
                viewModel.showSnackbar("Successfully added a friend!", "Success")
            }
        }
    }

    val handleDenyFriendRequest: (Int) -> Unit = { contactID ->
        viewModel.deleteData(StaySafe.Contact, contactID)
        viewModel.loadUserContent()
        viewModel.showSnackbar("Friend request denied!", "Success")
    }

    val searchFilteredUsers by remember(userFilter) {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                users
            } else {
                when (userFilter) {
                    "Username" -> users.filter {
                        it.UserUsername.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }

                    "First Name" -> users.filter {
                        it.UserFirstname.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }

                    "Last Name" -> users.filter {
                        it.UserLastname.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }

                    else -> users.filter {
                        it.UserUsername.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState.value) },
        topBar = {
            TopAppBar(
                title = { Text("Add Friend") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back arrow",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(text = "Search for a user") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    trailingIcon = {
                        FilterDropDown(userFilter, { userFilter = it })
                    },
                    singleLine = true
                )
            }
            LazyColumn {
                items(userRequests) { userRequest ->
                    FriendRequestCard(
                        userRequest,
                        onDeny = handleDenyFriendRequest,
                        onAccept = handleAcceptFriendRequest
                    )
                    HorizontalDivider()
                }
                items(searchFilteredUsers) { user ->
                    AddFriendCard(user, handleAddFriendRequest)
                    HorizontalDivider()
                }
            }
        }
    }
}


@Composable
fun FriendRequestCard(requester: User, onAccept: (Int, Int) -> Unit, onDeny: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffE28743)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(Modifier.size(110.dp)) {
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
                    text = "Friend Request:",
                    fontWeight = FontWeight.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Text(
                    text = requester.UserUsername,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = { onAccept(requester.UserID, requester.UserContactID) },
                        label = { Text("Accept") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Accept",
                            )
                        }
                    )
                    AssistChip(
                        onClick = { onDeny(requester.UserContactID) },
                        label = { Text("Deny") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Deny",
                            )
                        }
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
                containerColor = Color(0xff74A4E4),
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(Modifier.size(110.dp)) {
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
                    AssistChip(
                        onClick = { onAddFriendRequest(friend.UserID) },
                        label = { Text("Send Friend Request") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.add_friend),
                                contentDescription = "Add Friend",
                            )
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FilterDropDown(
        filterType: String,
        onFilter: (String) -> Unit
    ) {
        var selected by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = selected,
            onExpandedChange = { selected != !selected }
        ) {
            FilterChip(
                modifier = Modifier
                    .menuAnchor()
                    .padding(horizontal = 12.dp),
                selected = selected,
                onClick = { selected = !selected },
                label = { Text(text = filterType) },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.rotate(if (selected) 180f else 0f),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "List"
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = selected,
                onDismissRequest = { selected = false }
            ) {
                val filterList = listOf("Username", "First Name", "Last Name")
                filterList.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it) },
                        onClick = {
                            onFilter(it)
                            selected = false
                        }
                    )
                }
            }
        }
    }
