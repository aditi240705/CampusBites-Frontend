package com.simats.campusbites.responses

data class SignupResponse(
    val status: String,   // e.g., "success" or "error"
    val message: String,  // server message: "Signup successful", "Email already exists", etc.
    val user: User?       // Nullable, returned only on success
)