package com.example.recicleview.model

/**
 * Data class representing a historic building
 */
data class Building(
    val id: Int,
    val title: String,
    val category: String,
    val description: String,
    val imageResId: Int
)
