package com.example.themadproject.view.entity.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.model.data.Activity
import com.example.myapplication.model.data.User
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.R
import com.example.themadproject.view.Screen
import com.example.themadproject.view.entity.AddFriendCard
import com.example.themadproject.view.entity.FriendRequestCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityBottomSheet(
    onDismiss: () -> Unit,
    viewModel: StaySafeViewModel,
    navController: NavController,
    onActivitySelected: (Activity) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    ModalBottomSheet(
        modifier = Modifier.navigationBarsPadding(),
        sheetState = sheetState,
        scrimColor = Color.Transparent,
        onDismissRequest = onDismiss
    ) {
        TopAppBar(
            title = { Text("Activities") },
            actions = {
                IconButton(onClick = { navController.navigate(Screen.AddActivity.route) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Activity",
                    )
                }
            }
        )
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = (selectedTabIndex == 0),
                onClick = { selectedTabIndex = 0 },
                text = { Text("My Activities") }
            )
            Tab(
                selected = (selectedTabIndex == 1),
                onClick = { selectedTabIndex = 1 },
                text = { Text("Friend Activities") }
            )
        }

        when (selectedTabIndex) {
            0 -> ProfileActivityList(navController, onActivitySelected, viewModel)
            1 -> FriendsActivityList(navController, onActivitySelected, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileActivityList(
    navController: NavController,
    onActivitySelected: (Activity) -> Unit,
    viewModel: StaySafeViewModel
) {
    val activities = viewModel.userActivities.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            "My Activities",
            style = MaterialTheme.typography.titleLarge,
        )
        LazyColumn(
            modifier = Modifier.height(500.dp)
        ) {
            if (activities.isNotEmpty()) {
                items(activities) { activity ->
                    ProfileActivityCard(activity, onActivitySelected)
                }
            } else {
                item { Text("You have no activities") }
            }
        }
    }
}


@Composable
fun FriendsActivityList(
    navController: NavController,
    onActivitySelected: (Activity) -> Unit,
    viewModel: StaySafeViewModel
) {
    val activities = viewModel.friendActivities.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            "Friend Activities",
            style = MaterialTheme.typography.titleLarge,
        )
        LazyColumn(modifier = Modifier.height(500.dp)) {
            if (activities.isNotEmpty()) {
                items(activities) { activity ->
                    FriendActivityCard(activity, onActivitySelected)
                }
            } else {
                item { Text("There are no friend activities") }
            }
        }
    }
}


@Composable
fun ProfileActivityCard(activity: Activity, onActivitySelected: (Activity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffA3C9A8)
        ),
        onClick = { onActivitySelected(activity) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
        ) {
            Text(
                text = activity.ActivityName,
                fontWeight = FontWeight.Black,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
            Text(
                text = "${activity.ActivityFromName} to",
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
            Text(
                text = activity.ActivityToName ?: "",
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            Text(text = activity.ActivityStatusName ?: "")
        }
    }
}

@Composable
fun FriendActivityCard(activity: Activity, onActivitySelected: (Activity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffBBC2E2)
        ),
        onClick = { onActivitySelected(activity) }
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = activity.ActivityUsername ?: "",
            fontWeight = FontWeight.Black,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
        )
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xffCFCCD6)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
            ) {
                Text(
                    text = activity.ActivityName,
                    fontWeight = FontWeight.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Text(
                    text = "${activity.ActivityFromName} to",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Text(
                    text = activity.ActivityToName ?: "",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
        }
    }
}