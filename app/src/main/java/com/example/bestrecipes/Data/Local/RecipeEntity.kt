package com.example.bestrecipes.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val image: String,
    val imageType: String
)