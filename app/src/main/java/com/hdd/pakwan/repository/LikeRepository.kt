package com.hdd.pakwan.repository

import com.hdd.pakwan.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.pakwan.data.remoteDataSource.ServiceBuilder
import com.hdd.pakwan.data.remoteDataSource.response.LikeResponse
import com.hdd.pakwan.data.remoteDataSource.services.LikeService


class LikeRepository : HttpRequestNetworkCall() {
    private val likeService = ServiceBuilder.buildService(LikeService::class.java)

    suspend fun updateLike(postId:String): LikeResponse {
        return myHttpRequestNetworkCall {
            likeService.updateLike(ServiceBuilder.token!!,postId)
        }
    }
}

