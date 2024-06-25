package com.example.bestrecipes.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bestrecipes.Data.Responses.RecipeEntity


@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM favorite_recipes")
    fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM favorite_recipes WHERE isFavorite = 1 ORDER BY RANDOM() LIMIT 1")
    fun getRandomFavoriteRecipe(): RecipeEntity?

    @Query("UPDATE favorite_recipes SET isFavorite = :isFavorite WHERE id = :recipeId")
    fun updateFavoriteStatus(recipeId: Long, isFavorite: Boolean)

    @Query("DELETE FROM favorite_recipes WHERE id = :recipeId")
    fun deleteRecipe(recipeId: Long)

}