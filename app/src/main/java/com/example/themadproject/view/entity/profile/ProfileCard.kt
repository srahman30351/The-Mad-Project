package com.example.themadproject.view.entity.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.StaySafeViewModel

@Composable
fun ProfileCard(
    username: String,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    imageUrl: String,
    viewModel: StaySafeViewModel,
    onUpload: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffA3C9A8)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UploadPicture(imageUrl, viewModel, onUpload)
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
