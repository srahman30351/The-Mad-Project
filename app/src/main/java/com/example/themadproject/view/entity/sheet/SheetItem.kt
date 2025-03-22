package com.example.themadproject.view.entity.sheet

data class SheetItem(
    val label: String,
    val icon: Int,
    val state: Boolean,
    val onShow: () -> Unit
)