package com.example.themadproject.view.entity.marker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable

fun PinIconMarker(url: String) {
    Box(
        modifier = Modifier.size(100.dp).background(
            color = Color(0xffdcdcdc),
            shape = PinMarkerShape(38,40))
    ) {
        Card(
            shape = CircleShape,
            border = BorderStroke(4.dp, Color(0xffdcdcdc)),
            modifier = Modifier.padding(bottom = 20.dp).size(80.dp).align(Alignment.Center),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            AsyncImage(
                model = url,
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f / 1f)
            )

        }
    }
}