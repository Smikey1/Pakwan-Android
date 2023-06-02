package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.Notification


data class NotificationResponse(
    val success: Boolean? = null,
    val data: MutableList<Notification>? = null,
)