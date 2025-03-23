@file:Suppress("DEPRECATION")

package com.example.themadproject.view.entity.profile

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView.Guidelines
import com.example.myapplication.viewmodel.StaySafeViewModel
import com.example.themadproject.R

@Composable
fun UploadPicture(
    imageUrl: String,
    viewModel: StaySafeViewModel,
    onUpload: (String) -> Unit
) {

    val context = LocalContext.current
    var iconState by remember { mutableStateOf(true) }


    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { uri ->
                    viewModel.generateImageUrl(context, uri, onUpload)
                    iconState = false
            }
        }
    }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { selectedUri ->
                cropImageLauncher.launch(
                    CropImageContractOptions(
                        uri = selectedUri,
                        cropImageOptions = CropImageOptions(
                            guidelines = Guidelines.ON,
                            outputCompressFormat = Bitmap.CompressFormat.JPEG,
                            aspectRatioX = 1,
                            aspectRatioY = 1,
                            fixAspectRatio = true,
                            outputRequestWidth = 250,
                            outputRequestHeight = 250
                        )
                    )
                )
            }
        })

    Card(Modifier.size(140.dp)
    ) {
        Box (
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.clickable(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f / 1f)
            )
            if(iconState == true) {
                Icon(
                    painter = painterResource(R.drawable.upload_photo),
                    contentDescription = "Upload",
                    modifier = Modifier.padding(4.dp).size(36.dp)
                )
            }
        }
    }
}