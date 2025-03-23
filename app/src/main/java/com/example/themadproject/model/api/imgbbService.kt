package com.example.themadproject.model.api

import com.example.themadproject.model.data.Image
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface imgbbService {
    @Multipart
    @POST("1/upload")
    suspend fun uploadImage(
        @Query("key") key: String = "dc408d5ad40a34895b4b010e6f277264",
        @Query("expiration") expiration: Int = 0,
        @Part image: MultipartBody.Part
    ): Response<Image>
}