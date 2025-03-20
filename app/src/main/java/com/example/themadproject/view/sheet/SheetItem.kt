package com.example.themadproject.view.sheet

data class SheetItem(
    val label: String,
    val icon: Int,
    val state: Boolean,
    val onShow: () -> Unit
)