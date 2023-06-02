package com.hdd.pakwan.data.models

data class Notification(
    val _id:String="",
    val user: String?=null,
    val related: String?=null,
    val relatedPost: String?=null,
    val relatedRecipe: String?=null,
    val otherUser: User?=null,
    val message: String?= null,
    val createdAt: String?= null
)