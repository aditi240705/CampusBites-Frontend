package com.simats.campusbites

data class ComplaintResponse(
    val success: Boolean,
    val complaints: List<Complaint>
)

data class Complaint(
    val id: String,
    val transaction_id: String,
    val issue_type: String,
    val issue_details: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val canteen_name: String,
    val user_id: String
)

