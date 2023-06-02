package com.hdd.pakwan.data.remoteDataSource.response

import com.hdd.pakwan.data.models.Recipe


data class RecipesResponse(
    val success: Boolean? = null,
    val data: MutableList<Recipe>? = null,
)