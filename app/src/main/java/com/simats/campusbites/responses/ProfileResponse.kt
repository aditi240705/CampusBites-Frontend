package com.simats.campusbites.responses

data class ProfileResponse(
    val user: User,
    val orders: List<Order>
) {
    data class User(
        val user_id: Int,
        val username: String,
        val email: String,
        val phone_number: String,
        val role: String,
        val created_at: String
    )

    data class Order(
        val order_id: Int,
        val user_id: Int,
        val food_name: String,
        val canteen_name: String
    )
}
