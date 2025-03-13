package com.example.myapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.TrackerRepository
import com.example.myapplication.model.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackerViewModel(private val _repository: TrackerRepository) : ViewModel() {
    val users: StateFlow<List<User>> = _repository.fetchAllUsers.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun addUsers(users: List<User>) = viewModelScope.launch {
        _repository.insertUsers(users)
    }

    fun insertUser() = viewModelScope.launch {
        _repository.insertUser(_user.value!!)
    }

    fun updateUser() = viewModelScope.launch {
        _repository.updateUser(_user.value!!)
    }

    fun deleteUser() = viewModelScope.launch {
        _repository.deleteUser(_user.value!!)
    }
}