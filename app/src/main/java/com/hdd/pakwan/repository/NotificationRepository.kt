package com.hdd.pakwan.repository

import com.hdd.pakwan.data.models.Post
import com.hdd.pakwan.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.pakwan.data.remoteDataSource.ServiceBuilder
import com.hdd.pakwan.data.remoteDataSource.response.*
import com.hdd.pakwan.data.remoteDataSource.services.NotificationServices
import com.hdd.pakwan.data.remoteDataSource.services.PostServices
import okhttp3.MultipartBody
import okhttp3.RequestBody


class NotificationRepository : HttpRequestNetworkCall() {
    private val notificationService = ServiceBuilder.buildService(NotificationServices::class.java)

    suspend fun getNotification(): NotificationResponse {
        return myHttpRequestNetworkCall {
            notificationService.getNotification(ServiceBuilder.token!!)
        }
    }
}