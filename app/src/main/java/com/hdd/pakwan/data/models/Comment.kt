package com.hdd.pakwan.data.models

data class Comment(
    val _id:String="",
    val comment:String,
    val createdAt: String?=null,
    val post:String?=null,
    val user: User?=null,
)