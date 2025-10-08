package com.simats.campusbites.responses
import java.io.Serializable

data class MenuResponse(
    val status: String,
    val canteen: String,
    val menu: List<MenuItem>
)

data class MenuItem(
    val image: String?,
    val food_name: String,
    val price: String,
    val quantity: Int,
) : Serializable
