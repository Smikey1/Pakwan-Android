package com.hdd.pakwan.data.remoteDataSource.services

import com.hdd.pakwan.data.models.Post
import com.hdd.pakwan.data.remoteDataSource.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface NotificationServices {
    @GET("notification")
    suspend fun getNotification(
        @Header("Authorization") token: String,
    ): Response<NotificationResponse>
}
