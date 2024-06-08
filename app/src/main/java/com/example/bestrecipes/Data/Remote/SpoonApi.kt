package com.example.bestrecipes.Data.Remote

import com.example.bestrecipes.Data.Responses.Recipe
import com.example.bestrecipes.Data.Responses.RecipeList
import com.example.bestrecipes.Utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipeList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query(Constants.API_KEY) apiKey: String
    ): RecipeList

    @GET("recipes/{name}")
    suspend fun getRecipeDetails(
        @Path("name") name: String
    ): Recipe
}