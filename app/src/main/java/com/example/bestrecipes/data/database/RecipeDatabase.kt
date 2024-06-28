package com.example.bestrecipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bestrecipes.data.Converters
import com.example.bestrecipes.data.responses.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}