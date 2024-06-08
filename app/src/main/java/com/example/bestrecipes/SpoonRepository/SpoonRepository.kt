package com.example.bestrecipes.SpoonRepository

import com.example.bestrecipes.Data.Remote.SpoonApi
import com.example.bestrecipes.Data.Responses.Recipe
import com.example.bestrecipes.Data.Responses.RecipeList
import com.example.bestrecipes.Utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SpoonRepository @Inject constructor(
    private val api: SpoonApi
){
    suspend fun getRecipeList(limit: Int, offset: Int, apiKey: String): Resource<RecipeList> {
        val response = try {
            api.getRecipeList(limit, offset, apiKey)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    suspend fun getRecipeDetails(RecipeName: String): Resource<Recipe> {
        val response = try {
            api.getRecipeDetails(RecipeName)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }
}