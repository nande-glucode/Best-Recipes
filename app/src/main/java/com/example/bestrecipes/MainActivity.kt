package com.example.bestrecipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bestrecipes.Data.Responses.Instructions
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.HomeScreen.HomeScreen
import com.example.bestrecipes.RecipeDetail.RecipeDetailScreen
import com.example.bestrecipes.RecipeList.RecipeListScreen
import com.example.bestrecipes.ui.theme.BestRecipesTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BestRecipesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.Home.route
                ) {
                    composable(NavRoutes.Home.route) {
                        HomeScreen(navController = navController)
                    }
                    composable(NavRoutes.RecipeList.route) {
                        val recipeId = it.arguments?.getLong("id") ?: 0L
                        RecipeListScreen(navController = navController, recipeId = recipeId)
                    }
                    composable(
                        NavRoutes.RecipeDetail.route,
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) {
                        val id = it.arguments?.getLong("id") ?: 0L
                        val instructions = emptyList<Instructions>()
                        val recipeEntity = RecipeEntity(
                            id = id,
                            title = "",
                            image = "",
                            isFavorite = false,
                            readyInMinutes = 0,
                            servings = 0,
                            summary = "",
                            instructions = instructions ?: emptyList()
                        )
                        RecipeDetailScreen(recipeId = id, navController = navController, recipeEntity = recipeEntity)
                    }
                }
            }
        }
    }
}