package com.example.myapplication.adapter

data class FoodOnlineViewItem (
    val ChefName: String = "",
    val FoodName: String = "",
    val Topping: String = "",
    val ImageUrl: String = "",
    val Price: Double = 0.00,
    val Latitude: Double = 0.00,
    val Longitude: Double = 0.00
)