package com.example.bestrecipes.Data.Remote

import com.example.bestrecipes.Data.Local.RecipeEntity
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
        @Query("query") query: String? = null
    ): RecipeList

    @GET("recipes/{name}/information")
    suspend fun getRecipeDetails(
        @Path("name") name: String,
    ): RecipeEntity
}