package com.example.themadproject.view.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.data.User
import com.example.myapplication.viewmodel.StaySafeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendBottomSheet(
    onDismiss: () -> Unit,
    viewModel: StaySafeViewModel
){

    val users = viewModel.users.collectAsState().value
    if (users.isNotEmpty()) {
        val sheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            scrimColor = Color.Transparent,
            onDismissRequest = onDismiss
        ) {
            LazyColumn(
                modifier = Modifier.height(500.dp)
            ) {
                items(users) { user ->
                    FriendCard(user)
                }
            }
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
        onClick = { }
    ) {
        Column (
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
                fontWeight = FontWeight.Bold,
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