package com.simats.campusbites.responses

data class SignUpRequest(
    val name: String,
    val email: String,
    val phone_number: String,
    val password: String,
    val role: String   // Can be "user", "vendor", or "admin"
)