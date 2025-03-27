package com.example.myapplication.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
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
import com.example.themadproject.model.StaySafe
import com.example.themadproject.model.api.imgbbClient
import com.example.themadproject.model.data.Contact
import com.example.themadproject.model.data.ErrorMessage
import com.example.themadproject.model.tracking.LocationService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File

class StaySafeViewModel : ViewModel() {

    private val _locations = MutableStateFlow(emptyList<Location>())
    val locations: StateFlow<List<Location>> get() = _locations

    private val _positions = MutableStateFlow(emptyList<Position>())
    val positions: StateFlow<List<Position>> get() = _positions

    private val _status = MutableStateFlow(emptyList<Status>())
    val status: StateFlow<List<Status>> get() = _status

    private val _userActivities = MutableStateFlow(emptyList<Activity>())
    val userActivities: StateFlow<List<Activity>> get() = _userActivities

    private val _friendActivities = MutableStateFlow(emptyList<Activity>())
    val friendActivities: StateFlow<List<Activity>> get() = _friendActivities

    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> get() = _users

    private val _contactUsers = MutableStateFlow(emptyList<User>())
    val contactUsers: StateFlow<List<User>> get() = _contactUsers

    private val _requestUsers = MutableStateFlow(emptyList<User>())
    val requestUsers: StateFlow<List<User>> get() = _requestUsers

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user



    init {
        LocationService.ACTION_START
        getData(StaySafe.User)
    }

    fun loadUserContent() = viewModelScope.launch {
        _user.value?.let { user ->
            getActivities(user.UserID) { _userActivities.value = it }
            getFriendsByUserID(user.UserID)
            _requestUsers.value = emptyList()
            getFriendRequestsByUserID(user.UserID)
        }
    }

    //User specific functions -------------------------------------------------------------------

    fun setUser(user: User?) = viewModelScope.launch {
        _user.value = user
    }

    fun getUserUsername(username: String, onGet: ((User?) -> Unit)) = viewModelScope.launch {
        val response = StaySafeClient.api.getUsersByUsername(username)
        val userList = response.body()
        if (response.isSuccessful && !userList.isNullOrEmpty()) {
            //Grabs the first item from array as its assumed no user has the same username
            onGet(userList.first())
        } else {
            onGet(null)
        }
    }

    fun getFriendsByUserID(userID: Int) = viewModelScope.launch {
        val response = StaySafeClient.api.getUsers(userID, "Friend")
        val userList = response.body()
        if (response.isSuccessful && !userList.isNullOrEmpty()) {
            _contactUsers.value = userList
        }
    }

    fun addFriend(userID: Int, contactID: Int, onAdd: (() -> Unit)? = null) = viewModelScope.launch {
            _user.value?.let { profile ->
                val friendContact1 = Contact(
                    ContactUserID = userID,
                    ContactContactID = profile.UserID,
                    ContactLabel = "Friend",
                    ContactDatecreated = "2024-09-28T00:00:00.000Z",
                )
                _user.value?.let { profile ->
                    val friendContact2 = Contact(
                        ContactUserID = profile.UserID,
                        ContactContactID = userID,
                        ContactLabel = "Friend",
                        ContactDatecreated = "2024-09-28T00:00:00.000Z",
                    )
                    postData(friendContact1) {
                        postData(friendContact2) {
                            onAdd?.invoke()

                        }
                    }
            }
        }
    }

    fun getFriendRequestsByUserID(userID: Int) = viewModelScope.launch {
        val response = StaySafeClient.api.getUsers(userID, "Friend Request")
        val userList = response.body()
        if (response.isSuccessful && !userList.isNullOrEmpty()) {
            _requestUsers.value = userList
        } else {
            _requestUsers.value = emptyList()
        }
    }

    fun postLocation(location: Location, onPost: (Int) -> Unit) = viewModelScope.launch {
        val response = StaySafeClient.api.postLocation(location)
        if (response.isSuccessful) {
            val location = response.body()?.first()
            location?.LocationID?.let { locationID ->
                onPost(locationID)
            }
        }
    }

    fun getActivities(userID: Int, onGet: (List<Activity>) -> Unit) = viewModelScope.launch {
        val response = StaySafeClient.api.getActivitiesByUserID(userID)
        val activityList = response.body()
        if (response.isSuccessful && !activityList.isNullOrEmpty()) {
            onGet(activityList)
        }
    }
    fun updateActivity(updatedActivity: Activity, activityId: Int) {
        viewModelScope.launch {
            Log.d("StaySafe", "activity = $updatedActivity")
            putData(updatedActivity, activityId)
        }
            /*
            try {
                _userActivities.value = _userActivities.value.map { activity ->
                    if (activity.ActivityID == updatedActivity.ActivityID) updatedActivity else activity
                }
            } catch (e: Exception){
                Log.e("StaySafeViewModel", "Error updating activity: ${e.message} ")
            }
        }

     */
    }

    fun postFriendRequest(userID: Int, onPost: (() -> Unit)? = null) = viewModelScope.launch {
        _user.value?.let { profile ->
            val friendContact = Contact(
                ContactUserID = userID,
                ContactContactID = profile.UserID,
                ContactLabel = "Friend Request",
                ContactDatecreated = "2024-09-28T00:00:00.000Z",
            )
           postData(friendContact) {
                onPost?.invoke()
            }
        }
    }

    //Generalised StaySafe API call functions ---------------------------------------------------

    fun getData(from: StaySafe, onGet: (() -> Unit)? = null) = viewModelScope.launch {
        when (from) {
                is StaySafe.User -> {
                    StaySafeClient.api.getUsers().body()?.let {
                        _users.value = it
                        onGet?.invoke()
                    }
                }
                is StaySafe.Activity -> {
                    StaySafeClient.api.getActivities().body()?.let {
                        _userActivities.value = it
                        onGet?.invoke()
                    }
                }
                is StaySafe.Location -> {
                    StaySafeClient.api.getLocations().body()?.let {
                        _locations.value = it
                        onGet?.invoke()
                    }
                }
                is StaySafe.Status -> {
                    StaySafeClient.api.getStatus().body()?.let {
                        _status.value = it
                        onGet?.invoke()
                    }
                }
                is StaySafe.Position -> {
                    StaySafeClient.api.getPositions().body()?.let {
                        _positions.value = it
                        onGet?.invoke()
                    }
                }
            else -> { throw IllegalArgumentException("Post: Data type not supported") }
        }
    }

    fun postData(data: Any, onPost: (() -> Unit)? = null) = viewModelScope.launch {
        var response = when (data) {
            is User -> { StaySafeClient.api.postData(StaySafe.User.type, data) }
            is Activity -> { StaySafeClient.api.postData(StaySafe.Activity.type, data) }
            is Location -> { StaySafeClient.api.postData(StaySafe.Location.type, data) }
            is Status -> { StaySafeClient.api.postData(StaySafe.Status.type, data) }
            is Position -> { StaySafeClient.api.postData(StaySafe.Position.type, data) }
            is Contact -> { StaySafeClient.api.postData(StaySafe.Contact.type, data) }
            else -> { throw IllegalArgumentException("Post: Data type not supported") }
        }
        if (response.isSuccessful) {
            onPost?.invoke()
        } else {
            response.errorBody()?.let { errorBody ->
                getErrorResponse(errorBody)
            }
        }
    }

    fun putData(data: Any, dataID: Int, onPost: (() -> Unit)? = null) = viewModelScope.launch {
        var response = when (data) {
            is User -> { StaySafeClient.api.putData(StaySafe.User.type, dataID, data) }
            is Activity -> { StaySafeClient.api.putData(StaySafe.Activity.type, dataID, data) }
            is Location -> { StaySafeClient.api.putData(StaySafe.Location.type, dataID, data) }
            is Status -> { StaySafeClient.api.putData(StaySafe.Status.type, dataID, data) }
            is Position -> { StaySafeClient.api.putData(StaySafe.Position.type, dataID, data) }
            else -> { throw IllegalArgumentException("Post: Data type not supported") }
        }
        if (response.isSuccessful) {
            onPost?.invoke()
        } else {
            response.errorBody()?.let { errorBody ->
                getErrorResponse(errorBody)
            }
        }
    }

    fun deleteData(to: StaySafe, id: Int, onDelete: (() -> Unit)? = null) = viewModelScope.launch {
        val response = StaySafeClient.api.deleteData(to.type, id)
        if (response.isSuccessful) {
            onDelete?.invoke()
        } else {
            response.errorBody()?.let { errorBody ->
                getErrorResponse(errorBody)
            }
        }
    }

    private fun getErrorResponse(errorBody: ResponseBody) {
        var errorBody = errorBody.string()
            .replace("\"message\":\"", "\"message\":[\"")
            .replace("\"}", "\"]}")
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter(ErrorMessage::class.java)
        val errorMessages = adapter.fromJson(errorBody)
        errorMessages?.message?.forEach { message ->
            showPatientSnackbar(message, "Error")
        }
    }

    //imgbb API post and fetch response call --------------------------------------------------------------------

    fun generateImageUrl(context: Context, imageUri: Uri, onUpload: (String) -> Unit) =
        viewModelScope.launch {
            var file = File(context.cacheDir, "profile_picture.jpg")
            val inputStream = context.contentResolver.openInputStream(imageUri)
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                    val response = imgbbClient.api.uploadImage(image = body, expiration = 7889400)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onUpload(it.data.display_url)
                        }
                    } else {
                        response.errorBody()?.let {
                            showSnackbar("Upload failed", "Error")
                        }
                    }
                }
            }
        }

//Snackbar state --------------------------------------------------------------------------

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

fun showPatientSnackbar(message: String, action: String) = viewModelScope.launch {
    _snackbarHostState.value.showSnackbar(
        message = message,
        actionLabel = action,
        duration = SnackbarDuration.Short
    )
}
}