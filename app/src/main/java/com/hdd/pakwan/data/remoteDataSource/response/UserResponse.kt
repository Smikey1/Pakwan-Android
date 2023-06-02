package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.User


data class UserResponse(
    val success: Boolean? = null,
    val data: User? = null,
    val accessToken: String? = null,
    val message: String? = null
)