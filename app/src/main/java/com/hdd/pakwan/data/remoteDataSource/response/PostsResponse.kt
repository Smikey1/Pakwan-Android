package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.Post


data class PostsResponse(
    val success: Boolean? = null,
    val data: MutableList<Post>? = null,
)