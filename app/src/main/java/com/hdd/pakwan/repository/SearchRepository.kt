package com.hdd.pakwan.repository

import com.hdd.pakwan.data.models.Search
import com.hdd.pakwan.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.pakwan.data.remoteDataSource.ServiceBuilder
import com.hdd.pakwan.data.remoteDataSource.response.SearchResponse
import com.hdd.pakwan.data.remoteDataSource.services.SearchServices


class SearchRepository : HttpRequestNetworkCall() {
    private val searchService = ServiceBuilder.buildService(SearchServices::class.java)
    
    suspend fun search(search: Search): SearchResponse {
        return myHttpRequestNetworkCall {
            searchService.search(search)
        }
    }
}