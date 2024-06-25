package com.example.bestrecipes.Data.Remote

import com.example.bestrecipes.Data.Responses.Instructions
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.Data.Responses.RecipeList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipeList(
        @Query("number") limit: Int,
        @Query("offset") offset: Int,
        @Query("query") query: String? = null,
    ): RecipeList

    @GET("recipes/{id}/analyzedInstructions")
    suspend fun getRecipeInstructions(
        @Path("id") id: Long,
    ): List<Instructions>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Long,
    ): RecipeList
}