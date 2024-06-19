package com.example.bestrecipes.Data.Responses

import androidx.room.Entity
import androidx.room.PrimaryKey

 @Entity(tableName = "recipes")
 data class RecipeEntry(
        @PrimaryKey val id: Long,
        val title: String,
        val image: String,
     )