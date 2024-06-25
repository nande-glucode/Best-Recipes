package com.example.bestrecipes.SpoonRepository

import com.example.bestrecipes.Data.Remote.SpoonApi
import com.example.bestrecipes.Data.Responses.Instructions
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.Data.Responses.RecipeList
import com.example.bestrecipes.Database.RecipeDao
import com.example.bestrecipes.Utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            return Resource.Error("An unknown error occurred.")
        }
        return Resource.Success(response)
    }

    suspend fun getRecipeInformation(recipeId: Long): Resource<RecipeList> {
        val response = try {
            api.getRecipeInformation(recipeId)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    // Room database methods
      fun insertRecipe(recipe: RecipeEntity) {
          recipeDao.insertRecipe(recipe)
      }

    suspend fun getRandomFavoriteRecipe(): RecipeEntity? {
        return withContext(Dispatchers.IO) {
            recipeDao.getRandomFavoriteRecipe()
        }
    }

    suspend fun updateFavoriteStatus(recipeId: Long, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            recipeDao.updateFavoriteStatus(recipeId, isFavorite)
        }

    }

      fun deleteRecipe(recipeId: Long) {
          recipeDao.deleteRecipe(recipeId)
      }
}