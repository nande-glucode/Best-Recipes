package com.example.bestrecipes.SpoonRepository

import com.example.bestrecipes.Data.Remote.SpoonApi
import com.example.bestrecipes.Data.Responses.Recipe
import com.example.bestrecipes.Data.Responses.RecipeList
import com.example.bestrecipes.Utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SpoonRepository @Inject constructor(
    private val api: SpoonApi,
){
    suspend fun getRecipeList(limit: Int, offset: Int, query: String? = null): Resource<RecipeList> {
        val response = try {
            api.getRecipeList(limit, offset, query)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    suspend fun getRecipeDetails(recipeName: String): Resource<Recipe> {
        val response = try {
            api.getRecipeDetails(recipeName)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    // Room database methods
  /*  suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun getAllRecipes(): List<RecipeEntity> {
        return recipeDao.getAllRecipes()
    }

    suspend fun getRandomRecipe(): RecipeEntity? {
        return recipeDao.getRandomRecipe()
    }

    suspend fun deleteRecipe(recipeId: Long) {
        recipeDao.deleteRecipe(recipeId)
    } */
}