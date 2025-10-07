package com.saveetha.campusbites.responses

data class ReviewResponse(
    val status: String,
    val results: Int,
    val data: List<ReviewData>
)

data class ReviewData(
    val food_name: String,
    val canteen_name: String,
    val rating: String,
    val review: String,
    val review_date: String
)