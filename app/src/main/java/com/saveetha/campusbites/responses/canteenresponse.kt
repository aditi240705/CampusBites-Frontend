package com.saveetha.campusbites.responses

data class CanteenResponse(
    val canteens: List<Canteen>
)

data class Canteen(
    val canteen_id: String,
    val canteen_name: String
)
