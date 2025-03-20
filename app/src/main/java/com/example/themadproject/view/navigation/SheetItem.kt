package com.example.myapplication.view.navigation

import androidx.compose.runtime.MutableState

data class SheetItem(
    val label: String,
    val icon: Int,
    val state: Boolean,
    val onShow: () -> Unit
)
