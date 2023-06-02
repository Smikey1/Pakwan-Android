package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.Search


data class SearchResponse(
    val success: Boolean? = null,
    val data: Search? = null,
    val message: String? = null
)