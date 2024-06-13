package com.example.bestrecipes.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bestrecipes.Data.Local.RecipeEntity


@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM recipes ORDER BY RANDOM() LIMIT 1")
    fun getRandomRecipe(): RecipeEntity?

    @Query("DELETE FROM recipes WHERE id = :recipeId")
    fun deleteRecipe(recipeId: Long)
}