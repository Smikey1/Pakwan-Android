package com.hdd.pakwan.data.models


data class Recipe(
    var _id:String="",
//    var user: User? = null,
    var title:String? = null,
    var avgRating:Int? = null,
    var description:String? = null,
    var image: String? = null,
    var archive:String? = null,
    var createdDate:String? = null,
    var updatedDate:String? = null,
    var preparation:PreparationSchema? = null,
    var direction:MutableList<String>? = null,
    var hashtag:MutableList<String>? = null,
    var ingredients: MutableList<IngredientSchema>? = null,
    var review:MutableList<ReviewRating>?=null
)

data class IngredientSchema(
    var quantity:Int? = null,
    var unit:String? = null,
    var item:String? = null,
)

data class PreparationSchema(
    var preparation:Int? = null,
    var cooking:Int? = null,
    var serving:Int? = null,
    var yield: Int? = null
)