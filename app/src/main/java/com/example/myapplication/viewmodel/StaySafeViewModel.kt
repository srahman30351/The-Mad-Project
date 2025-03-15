package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.api.StaySafeClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StaySafeViewModel : ViewModel() {
    private val _staySafeData = MutableStateFlow("No data")
    val staySafeData: StateFlow<String> get() = _staySafeData

    init {
        viewModelScope.launch {
            getLocations()
        }
    }

    private fun getLocations() = viewModelScope.launch {
        _staySafeData.value = StaySafeClient.api.getLocations().toString()
    }

//    fun getLocations() = viewModelScope.launch {
//        _staySafeData.value = StaySafeClient.StaySafeService.getLocations().toString()
//    }
//
//    private val _user = MutableStateFlow<User?>(null)
//    val user: StateFlow<User?> = _user
//
//    fun addUsers(users: List<User>) = viewModelScope.launch {
//    }
//
//    fun insertUser() = viewModelScope.launch {
//    }
//
//    fun updateUser() = viewModelScope.launch {
//    }
//
//    fun deleteUser() = viewModelScope.launch {
//    }
}