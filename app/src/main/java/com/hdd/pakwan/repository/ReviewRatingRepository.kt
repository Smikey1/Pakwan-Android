package com.hdd.pakwan.repository

import com.hdd.pakwan.data.models.ReviewRating
import com.hdd.pakwan.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.pakwan.data.remoteDataSource.ServiceBuilder
import com.hdd.pakwan.data.remoteDataSource.response.ReviewRatingResponse
import com.hdd.pakwan.data.remoteDataSource.services.ReviewRatingService


class ReviewRatingRepository : HttpRequestNetworkCall() {
    private val reviewService = ServiceBuilder.buildService(ReviewRatingService::class.java)

    suspend fun getAllReviewRating(recipeId:String): ReviewRatingResponse {
        return myHttpRequestNetworkCall {
            reviewService.getAllReviewRating(recipeId)
        }
    }

    suspend fun addReviewRating(reviewId:String,review:ReviewRating): ReviewRatingResponse {
        return myHttpRequestNetworkCall {
            reviewService.addReviewRating(ServiceBuilder.token!!,reviewId,review)
        }
    }

    suspend fun updateReviewRating(recipeId:String,review:ReviewRating): ReviewRatingResponse {
        return myHttpRequestNetworkCall {
            reviewService.updateReviewRating(ServiceBuilder.token!!,recipeId,review)
        }
    }

    suspend fun deleteReviewRating(recipeId:String,reviewId:String): ReviewRatingResponse {
        return myHttpRequestNetworkCall {
            reviewService.deleteReviewRating(ServiceBuilder.token!!,recipeId,reviewId)
        }
    }
}