package com.example.bestrecipes.Data.Responses

data class RecipeList(
    val results: List<Result>,
    val offset: Long,
    val number: Long,
    val totalResults: Long,
)

data class Result(
    val id: Long,
    val title: String,
    val image: String,
    val imageType: String,
)

