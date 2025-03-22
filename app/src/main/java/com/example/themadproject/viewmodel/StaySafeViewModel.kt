package com.example.myapplication.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.api.StaySafeClient
import com.example.myapplication.model.api.StaySafeService
import com.example.myapplication.model.data.Activity
import com.example.myapplication.model.data.Location
import com.example.myapplication.model.data.Position
import com.example.myapplication.model.data.Status
import com.example.myapplication.model.data.User
import com.example.themadproject.model.api.imgbbClient
import com.example.themadproject.model.data.ErrorMessage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

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

    /*
        fun uploadImage(imageUri: Uri) {
            val file = File(imageUri.path ?: "")

            val mediaType = "image/*".toMediaTypeOrNull()
            val requestBody = file.asRequestBody(mediaType)
            val multipartBodyPart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestBody
            )
           val response = imgbbClient.api.uploadImage(image = multipartBodyPart)
            println(response.body())

        }

     */
     */
    fun setUser(user: User?) = viewModelScope.launch {
        _user.value = user
    }

    fun findUser(username: String, onResult: (Boolean) -> Unit) = viewModelScope.launch {
        val response = StaySafeClient.api.getUsersByUsername(username)
        //Since the API does not handle unique usernames, the first user will be picked if there's a duplicate
        if(response.isSuccessful) _user.value = response.body()?.first()
        onResult(response.isSuccessful)
    }

    fun createUser(user: User, onCreate: () -> Unit) = viewModelScope.launch {
        val response = StaySafeClient.api.postUser(user)
        if (response.isSuccessful) {
            onCreate()
        } else {
            response.errorBody()?.let { errorBody ->
                var errorBody = errorBody.string()
                    .replace("\"message\":\"", "\"message\":[\"")
                    .replace("\"}", "\"]}")
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val adapter = moshi.adapter(ErrorMessage::class.java)
                val errorMessages = adapter.fromJson(errorBody)
                errorMessages?.message?.forEach {
                    showPatientSnackbar(it, "Error")
                }
            }
        }
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        try {
            // Open the content resolver to get the input stream
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

            // Generate a temporary file to store the content
            val tempFile = File(context.cacheDir, "temp_file")

            // Write the content to the temporary file
            val outputStream = FileOutputStream(tempFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream?.read(buffer).also { length = it ?: -1 } != -1) {
                outputStream.write(buffer, 0, length)
            }

            // Close streams
            inputStream?.close()
            outputStream.close()

            return tempFile

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun generateImageUrl(
        context: Context,
        imageUri: Uri?,
        onUpload: (String) -> Unit
    ) = viewModelScope.launch {
        if (imageUri != null) {
            val file = getFileFromUri(context, imageUri)
            file?.let {
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                val response = imgbbClient.api.uploadImage(image = body, expiration = 7889400)
                if (response.isSuccessful) {
                    response.body()?.let {
                        onUpload(it.data.display_url)
                    }
                } else {
                    response.errorBody()?.let {
                        showSnackbar(it.string(), "Error")
                    }
                }
            }
        }
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

    fun showPatientSnackbar(message: String, action: String) = viewModelScope.launch {
        _snackbarHostState.value.showSnackbar(
            message = message,
            actionLabel = action,
            duration = SnackbarDuration.Short
        )
    }
}