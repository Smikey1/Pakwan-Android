package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.Comment

data class CommentsResponse(
    val success: Boolean? = null,
    val data: MutableList<Comment>? = null,
)