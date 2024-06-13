package com.example.bestrecipes.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bestrecipes.Data.Local.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}