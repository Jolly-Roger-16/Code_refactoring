package com.example.practica_1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
)