package com.example.bestrecipes.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.Data.Responses.RecipeEntry

@Database(entities = [RecipeEntry::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}