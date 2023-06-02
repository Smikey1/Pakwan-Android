package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.Recipe


data class RecipeResponse(
    val success: Boolean? = null,
    val data: Recipe? = null,
)