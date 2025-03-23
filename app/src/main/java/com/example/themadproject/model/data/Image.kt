package com.example.themadproject.model.data

data class Image(
    val data: ImageData,
)

data class ImageData(
    val title: String,
    val url_viewer: String,
    val url: String,
    val display_url: String,
    val expiration: Int
)