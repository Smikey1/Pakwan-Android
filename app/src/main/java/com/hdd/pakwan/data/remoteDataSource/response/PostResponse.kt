package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.Post


data class PostResponse(
    val success: Boolean? = null,
    val data: Post? = null,
    val accessToken: String? = null
)