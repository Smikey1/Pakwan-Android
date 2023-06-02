package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.User


data class FollowersFollowingResponse(
    val success: Boolean? = null,
    val data: MutableList<User>? = null,
    val accessToken: String? = null,
    val message: String? = null
)