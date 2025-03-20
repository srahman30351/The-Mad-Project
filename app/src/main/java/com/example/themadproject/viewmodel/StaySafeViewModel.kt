package com.example.myapplication.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.api.StaySafeClient
import com.example.myapplication.model.data.Activity
import com.example.myapplication.model.data.Location
import com.example.myapplication.model.data.Position
import com.example.myapplication.model.data.Status
import com.example.myapplication.model.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StaySafeViewModel : ViewModel() {

    private val _activities = MutableStateFlow(emptyList<Activity>())
    val activities: StateFlow<List<Activity>> get() = _activities

    private val _locations = MutableStateFlow(emptyList<Location>())
    val locations: StateFlow<List<Location>> get() = _locations

    private val _positions = MutableStateFlow(emptyList<Position>())
    val positions: StateFlow<List<Position>> get() = _positions

    private val _status = MutableStateFlow(emptyList<Status>())
    val status: StateFlow<List<Status>> get() = _status

    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> get() = _users

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    init {
        viewModelScope.launch {
            getLocations()
            getUsers()
            getActivities()
        }
    }

    fun setUser(user: User?) = viewModelScope.launch {
        _user.value = user
    }

    private fun getLocations() = viewModelScope.launch {
        _locations.value = StaySafeClient.api.getLocations()
    }

    private fun getActivities() = viewModelScope.launch {
        _activities.value = StaySafeClient.api.getActivities()
    }

    private fun getPositions() = viewModelScope.launch {
        _positions.value = StaySafeClient.api.getPositions()
    }

    private fun getStatus() = viewModelScope.launch {
        _status.value = StaySafeClient.api.getStatus()
    }

    private fun getUsers() = viewModelScope.launch {
        _users.value = StaySafeClient.api.getUsers()
    }

    private val _snackbarHostState = mutableStateOf(SnackbarHostState())
    val snackbarHostState: State<SnackbarHostState> get() = _snackbarHostState

    fun showSnackbar(message: String, action: String) = viewModelScope.launch {
        _snackbarHostState.value.currentSnackbarData?.dismiss()
        _snackbarHostState.value.showSnackbar(
            message = message,
            actionLabel = action,
            duration = SnackbarDuration.Short
        )
    }
}