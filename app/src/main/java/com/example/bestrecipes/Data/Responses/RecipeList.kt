package com.example.bestrecipes.Data.Responses

data class RecipeList(
    val results: List<RecipeEntity>,
    val offset: Long,
    val number: Long,
    val totalResults: Long,
)

data class RecipeEntity(
    val id: Long,
    val title: String,
    val image: String,
    val instructions: List<Instructions>
)

data class Instructions(
    val name: String,
    val steps: List<Step>,
)

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredient>,
    val number: Long,
    val step: String,
    val length: Length?,
)

data class Equipment(
    val id: Long,
    val image: String,
    val name: String,
    val temperature: Temperature?,
)

data class Temperature(
    val number: Double,
    val unit: String,
)

data class Ingredient(
    val id: Long,
    val image: String,
    val name: String,
)

data class Length(
    val number: Long,
    val unit: String,
)
