package com.saveetha.campusbites.responses

data class LoginResponse(
    val status: String,
    val message: String,
    val user: User?
)

data class User(
    val user_id: Int,
    val username: String,
    val name: String,
    val email: String,
    val role : String
)