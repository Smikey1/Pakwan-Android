package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.Restaurant


data class RestaurantResponse(
    val success: Boolean? = null,
    val data: Restaurant? = null,
)