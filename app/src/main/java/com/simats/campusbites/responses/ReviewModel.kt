package com.simats.campusbites.responses

data class ReviewModel(
    val food_name: String,
    val canteen_name: String,
    val rating: String,
    val review: String,
    val review_date: String
)
