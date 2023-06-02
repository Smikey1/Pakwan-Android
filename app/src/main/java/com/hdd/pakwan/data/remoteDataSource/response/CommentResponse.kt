package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.Comment

data class CommentResponse(
    val success: Boolean? = null,
    val data: Comment? = null,
)