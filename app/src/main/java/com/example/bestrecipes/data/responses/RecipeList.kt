package com.example.bestrecipes.data.responses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.bestrecipes.data.Converters

data class RecipeList(
    val results: List<RecipeEntity>,
    val offset: Long,
    val number: Long,
    val totalResults: Long,
)

@Entity(tableName = "favorite_recipes")
data class RecipeEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "readyInMinutes") val readyInMinutes: Int,
    @ColumnInfo(name = "servings") val servings: Int,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean,
    @ColumnInfo @TypeConverters(Converters::class) val instructions: List<Instructions>
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
