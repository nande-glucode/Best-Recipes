package com.example.bestrecipes.SpoonRepository

import com.example.bestrecipes.Data.Remote.SpoonApi
import com.example.bestrecipes.Data.Responses.Instructions
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.Data.Responses.RecipeEntry
import com.example.bestrecipes.Data.Responses.RecipeList
import com.example.bestrecipes.Database.RecipeDao
import com.example.bestrecipes.Utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SpoonRepository @Inject constructor(
    private val api: SpoonApi,
    private val recipeDao: RecipeDao
){
    suspend fun getRecipeList(number: Int, offset: Int, query: String? = null): Resource<RecipeList> {
        val response = try {
            api.getRecipeList(number, offset, query)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    suspend fun getRecipeInstructions(recipeId: Long): Resource<List<Instructions>> {
        val response = try {
            api.getRecipeInstructions(recipeId)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    // Room database methods
      fun insertRecipe(recipe: RecipeEntry) {
          recipeDao.insertRecipe(recipe)
      }

      fun getAllRecipes(): List<RecipeEntry> {
          return recipeDao.getAllRecipes()
      }

      fun getRandomFavoriteRecipe(): RecipeEntry? {
          return recipeDao.getRandomRecipe()
      }

      fun deleteRecipe(recipeId: Long) {
          recipeDao.deleteRecipe(recipeId)
      }
}