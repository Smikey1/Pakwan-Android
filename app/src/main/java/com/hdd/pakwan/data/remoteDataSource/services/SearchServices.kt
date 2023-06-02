package com.hdd.pakwan.data.remoteDataSource.services

import com.hdd.pakwan.data.models.Search
import com.hdd.pakwan.data.remoteDataSource.response.SearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchServices {
    @POST("search")
    suspend fun search(
        @Body search: Search,
    ): Response<SearchResponse>
}